/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import net.rubyeye.xmemcached.MemcachedClient;
//import net.rubyeye.xmemcached.exception.MemcachedException;

import net.spy.memcached.MemcachedClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.matrix.framework.core.collection.Key;
import org.matrix.framework.core.database.memcached.MatrixMemcachedFactory;
import org.matrix.framework.core.database.memcached.MatrixMemcachedGroup;
import org.matrix.framework.core.log.LoggerFactory;
import org.matrix.framework.core.platform.SpringObjectFactory;
import org.matrix.framework.core.platform.exception.MatrixException;
import org.matrix.framework.core.platform.web.cookie.CookieSpec;
import org.matrix.framework.core.platform.web.velocity.VelocityAccess;
import org.matrix.framework.core.util.ExceptionUtils;
import org.matrix.framework.core.util.TimeLength;

public abstract class MemcachedPageFilter implements Filter {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PageInfo.class);
//    private final Logger logger = LoggerFactory.getLogger(MemcachedPageFilter.class);

    private static final TimeLength MEMCACHED_TIME_OUT = TimeLength.seconds(3);

    public static final String ATTRIBUTE_ID = "id";

    public static final String ATTRIBUTE_TEMPLATE = "template";

    public static final String DYNAMIC_TAG = "Dynamic";

    protected static final int DELETE_COOKIE_MAX_AGE = 0;
    
    final Map<String, TimeLength> cacheMap = new ConcurrentHashMap<String, TimeLength>();

    protected MemcachedClient memcachedClient;
    
    protected String servers;
    
    protected VelocityAccess velocityAccess;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.registerCache(new CacheRegisterImpl(this));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        TimeLength cacheTime = getTimeLength(httpRequest);
        if (null == cacheTime) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        // TODO:initialize memcachedClient;
        if (null == memcachedClient) {
            MatrixMemcachedFactory matrixMemcachedFactory = SpringObjectFactory.getInstance().getBean(MatrixMemcachedFactory.class);
            memcachedClient = matrixMemcachedFactory.getClient(MatrixMemcachedGroup.PAGEGROUP.toString());
            servers = matrixMemcachedFactory.getServers(MatrixMemcachedGroup.PAGEGROUP.toString());
            velocityAccess = SpringObjectFactory.getInstance().getBean(VelocityAccess.class);
        }

        //取出来的值可能是已经被替换后的，不是完整的页面。
        PageInfo pageInfo = buildPageInfo(httpRequest, httpResponse, cacheTime, chain);
        if (pageInfo.isOk()) {
            writeResponse(httpRequest, httpResponse, pageInfo);
        }
    }

    private void writeResponse(final HttpServletRequest request, final HttpServletResponse response, final PageInfo pageInfo) throws ResponseHeadersNotModifiableException, IOException {
        setStatus(response, pageInfo);
        setContentType(response, pageInfo);
        setHeaders(pageInfo, response);
        writeContent(request, response, pageInfo);
    }

    private void writeContent(final HttpServletRequest request, final HttpServletResponse response, final PageInfo pageInfo) throws IOException, ResponseHeadersNotModifiableException {
        byte[] body;
        boolean shouldBodyBeZero = ResponseUtil.shouldBodyBeZero(request, pageInfo.getStatusCode());
        if (shouldBodyBeZero) {
            body = new byte[0];
        } else if (acceptsGzipEncoding(request)) {
            body = pageInfo.getGzippedBody();
            if (ResponseUtil.shouldGzippedBodyBeZero(body, request)) {
                body = new byte[0];
            } else {
                ResponseUtil.addGzipHeader(response);
                //TODO:1.解压，2替换，3压缩， 4输出 
                body = reverseRelaceCache(request, body, true);
            }
        } else {
            //TODO:1.替换，2输出
            body = pageInfo.getUngzippedBody();
            body = reverseRelaceCache(request, body, false);
        }
        response.setContentLength(body.length);
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(body);
        out.flush();
    }

    private boolean acceptsGzipEncoding(HttpServletRequest request) {
        return headerContains(request, "Accept-Encoding", "gzip");
    }

    private boolean headerContains(final HttpServletRequest request, final String header, final String value) {
        final Enumeration accepted = request.getHeaders(header);
        while (accepted.hasMoreElements()) {
            final String headerValue = (String) accepted.nextElement();
            if (headerValue.indexOf(value) != -1) {
                return true;
            }
        }
        return false;
    }

    // do not cache the cookies!
    private PageInfo buildPageInfo(final HttpServletRequest request, final HttpServletResponse response, final TimeLength cacheTime, final FilterChain chain) throws IOException, ServletException {
        return buildPageInfo(request, response, cacheTime, chain, 0);
    }

    private PageInfo buildPageInfo(final HttpServletRequest request, final HttpServletResponse response, final TimeLength cacheTime, final FilterChain chain, int counter) throws IOException, ServletException {
        final String cacheKey = getCacheKey(request);
        PageInfo pageInfo = null;
        Object cacheData = null;
        try {
            cacheData = memcachedClient.get(cacheKey);
        } catch (Exception e) {
            if (counter > 3) {
                LoggerFactory.MEMCACHEDLOGGER.getLogger().error("MEMCACHED SERVERS:[" + servers + "], pagecache buildPageInfo operition try 3 times failed;" + ExceptionUtils.stackTrace(e));
                throw new MatrixException("Operition memcached timeout,detail in log file.");
            }
            return buildPageInfo(request, response, cacheTime, chain, counter + 1);
        }
        if (cacheData == null) {
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            final GenericResponseWrapper wrapper = new GenericResponseWrapper(response, outStream);
            chain.doFilter(request, wrapper);
            wrapper.flush();

            //分析动态区将动态区的内容替换成空字符串。
            boolean shouldBodyBeZero = ResponseUtil.shouldBodyBeZero(request, response.getStatus());
            byte[] body = outStream.toByteArray();
            
            if (!shouldBodyBeZero) {
                body = this.replaceCache(body); 
            }

            pageInfo = new PageInfo(wrapper.getStatus(), wrapper.getContentType(), body, true, wrapper.getAllHeaders());

            if (pageInfo.isOk()) {
                try {
                    memcachedClient.set(cacheKey, (int) cacheTime.toSeconds(), pageInfo);
                } catch (Exception e) {
                    if (counter > 3) {
                        LoggerFactory.MEMCACHEDLOGGER.getLogger().error("MEMCACHED SERVERS:[" + servers + "], pagecache buildPageInfo operition try 3 times failed;" + ExceptionUtils.stackTrace(e));
                        throw new MatrixException("Operition memcached exception,detail in log file.");
                    }
                    return buildPageInfo(request, response, cacheTime, chain, counter + 1);
                }
            }
        } else
            pageInfo = (PageInfo) cacheData;
        return pageInfo;
    }

    private void setContentType(final HttpServletResponse response, final PageInfo pageInfo) {
        String contentType = pageInfo.getContentType();
        if (contentType != null && contentType.length() > 0) {
            response.setContentType(contentType);
        }
    }

    public <T> void deleteCookie(Key<T> cookieKey, CookieSpec cookieSpec, final HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie(cookieKey.name(), null);
        cookie.setMaxAge(DELETE_COOKIE_MAX_AGE);
        cookie.setPath(cookieSpec.getPath());
        String domain = cookieSpec.getDomain();
        if (StringUtils.hasText(domain))
            cookie.setDomain(domain);
        httpServletResponse.addCookie(cookie);
    }

    private void setStatus(final HttpServletResponse response, final PageInfo pageInfo) {
        response.setStatus(pageInfo.getStatusCode());
    }

    private void setHeaders(final PageInfo pageInfo, final HttpServletResponse response) {
        final Collection<Header<? extends Serializable>> headers = pageInfo.getHeaders();
        final TreeSet<String> setHeaders = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        for (final Header<? extends Serializable> header : headers) {
            final String name = header.getName();
            switch (header.getType()) {
                case STRING:
                    if (setHeaders.contains(name)) {
                        response.addHeader(name, (String) header.getValue());
                    } else {
                        setHeaders.add(name);
                        response.setHeader(name, (String) header.getValue());
                    }
                    break;
                case DATE:
                    if (setHeaders.contains(name)) {
                        response.addDateHeader(name, (Long) header.getValue());
                    } else {
                        setHeaders.add(name);
                        response.setDateHeader(name, (Long) header.getValue());
                    }
                    break;
                case INT:
                    if (setHeaders.contains(name)) {
                        response.addIntHeader(name, (Integer) header.getValue());
                    } else {
                        setHeaders.add(name);
                        response.setIntHeader(name, (Integer) header.getValue());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("No mapping for Header: " + header);
            }
        }
    }

    private String getCacheKey(HttpServletRequest httpRequest) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpRequest.getMethod()).append(httpRequest.getRequestURI()).append(httpRequest.getQueryString());
        return stringBuffer.toString();
    }

    private byte[] replaceCache(byte[] pageBody) throws IOException {
        byte[] httpBody = pageBody;
        
        boolean isGzipped = PageInfo.isGzipped(httpBody);
        if (isGzipped) {
            httpBody = PageInfo.ungzip(httpBody);
        }
        String pageBodyContent = new String(httpBody, "UTF-8");
        Document document = Jsoup.parse(pageBodyContent);
        Elements dynamicElements = document.getElementsByTag(DYNAMIC_TAG);
        if (!dynamicElements.isEmpty()) {
            for (Element dynamicElement : dynamicElements) {
                dynamicElement.empty();
            }
        }
        byte[] orignalBody = document.toString().getBytes("UTF-8");
        return isGzipped ? PageInfo.gzip(orignalBody) : orignalBody;
    }
 
    private byte[] reverseRelaceCache(final HttpServletRequest request, byte[] pageBody , boolean isGzipped) throws IOException {
        byte[] httpBody = pageBody;
        
        if (isGzipped)  httpBody = PageInfo.ungzip(httpBody);
        String pageBodyContent = new String(httpBody, "UTF-8");
        
        final Map<String, DynamicElement> dynamicMap = new HashMap<String, DynamicElement>();
        
        Document document = Jsoup.parse(pageBodyContent);
        Elements dynamicElements = document.getElementsByTag(DYNAMIC_TAG);

        if (!dynamicElements.isEmpty()) {
            for (Element dynamicElement : dynamicElements) {
                String id = dynamicElement.attr(ATTRIBUTE_ID);
                String templateName = dynamicElement.attr(ATTRIBUTE_TEMPLATE);
                dynamicMap.put(id, new DynamicElement(templateName, dynamicElement));
            }
            fillDynamicData(request, dynamicMap);
        }
        byte[] orignalBody = document.toString().getBytes("UTF-8");
        return isGzipped ? PageInfo.gzip(orignalBody) : orignalBody;
    }
    
    public TimeLength getTimeLength(final HttpServletRequest request) {
        if (CollectionUtils.isEmpty(cacheMap)) return null;
        String uri = request.getRequestURI().replaceAll(request.getContextPath(), "");
        Set<Entry<String, TimeLength>> entrySet = cacheMap.entrySet();
        for (Entry<String, TimeLength> entry : entrySet) {
            String key = entry.getKey();
            TimeLength timeLength = entry.getValue();
            if (Pattern.compile(key).matcher(uri).matches()) return timeLength;
            continue;
        }
        return null;
    }
    
    public VelocityAccess getVelocityAccess() {
        return velocityAccess;
    }

    public abstract void registerCache(CacheRegister cacheRegister);
    
    public abstract void fillDynamicData(final HttpServletRequest request, final Map<String, DynamicElement> dynamicMap);

}