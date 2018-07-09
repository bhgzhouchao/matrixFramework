/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeLengthTest {
    @Test
    public void toMilliseconds() {
        assertEquals(1000, TimeLength.seconds(1).toMilliseconds());

        assertEquals(60000, TimeLength.minutes(1).toMilliseconds());

        assertEquals(0, TimeLength.ZERO.toMilliseconds());
    }

    @Test
    public void oneMinuteEqualsSixtySeconds() {
        assertEquals(TimeLength.minutes(1), TimeLength.seconds(60));
    }

    @Test
    public void oneHourEqualsSixtyMinutes() {
        assertEquals(TimeLength.hours(1), TimeLength.minutes(60));
    }

    @Test
    public void toSeconds() {
        assertEquals(60, TimeLength.minutes(1).toSeconds());
    }
}
