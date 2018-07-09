/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.session;

import org.matrix.framework.core.collection.Key;
import org.matrix.framework.core.collection.KeyMap;
import org.matrix.framework.core.util.ReadOnly;

public class SessionContext {
    private final ReadOnly<String> id = new ReadOnly<String>();
    private final KeyMap session = new KeyMap();
    private boolean changed;
    private boolean invalidated;

    public <T> T get(Key<T> key) {
        return session.get(key);
    }

    public <T> void set(Key<T> key, T value) {
        session.put(key, value);
        changed = true;
    }

    public void invalidate() {
        session.clear();
        changed = true;
        invalidated = true;
    }

    boolean changed() {
        return changed;
    }

    boolean invalidated() {
        return invalidated;
    }

    String getId() {
        return id.value();
    }

    void setId(String id) {
        this.id.set(id);
    }

    void loadSessionData(String sessionData) {
        session.deserialize(sessionData);
    }

    String getSessionData() {
        return session.serialize();
    }

    void saved() {
        changed = false;
    }

    void requireNewSessionId() {
        changed = true;
    }
}
