package com.zhichan.msmds;

import com.zhichan.openadsdk.holder.AdType;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String adType = "adnet";
        AdType type = AdType.fromTypeName(adType);
        switch (type) {
            case UNION:
                System.out.println(type.getTypeName());
                break;
            case ADNET:
                System.out.println("adnet");
                break;
        }
    }
}