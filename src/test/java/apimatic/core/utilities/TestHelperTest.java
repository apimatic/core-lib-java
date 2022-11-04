package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.apimatic.core.utilities.TestHelper;

public class TestHelperTest {

    private static final int BIG_NUMBER4 = 1234576;
    private static final int BIG_NUMBER3 = 123434556;
    private static final int BIG_NUMBER2 = 1234526596;
    private static final int BIG_NUMBER1 = 123456;
    private static final String TEST_FILE_PATH =
            "https://gist.githubusercontent.com/asadali214"
                    + "/0a64efec5353d351818475f928c50767/raw/8ad3533"
                    + "799ecb4e01a753aaf04d248e6702d4947/testFile.txt";

    @Test
    public void testIsJsonProperSubsetOf() throws IOException {
        String leftObject = "{\"passed\":true}";
        String rightObject =
                "{\"passed\":true,\"message\":\"OK\",\"input\":"
                        + "{\"path\":\"/query/rfc1123datetimearray?"
                        + "datetimes[0]=Sun%2C+06+Nov+1994+08%3A49%3A37+GMT&"
                        + "datetimes[1]=Sun%2C+06+Nov+1994+08%3A49%3A37+GMT\","
                        + "\"query\":{\"datetimes\":[\"Sun, 06 Nov 1994 "
                        + "08:49:37 GMT\",\"Sun, 06 Nov 1994 08:49:37 GMT\"]}"
                        + ",\"headers\":{\"accept\":\"application/json\",\"host\""
                        + ":\"localhost:3000\",\"connection\":\"Keep-Alive\","
                        + "\"accept-encoding\":\"gzip\",\"user-agent\""
                        + ":\"okhttp/4.9.1\"},\"method\":\"GET\",\"body\":{},"
                        + "\"uploadCount\":0}}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, true, false);
        assertTrue(actual);
    }

    @Test
    public void testIsJsonProperSubsetOfList() throws IOException {
        String leftObject =
                "{\"daysvsString\":\"Sunday\",\"allOneOf\":2,"
                        + "\"allOuterArray\":[2,\"February\"],"
                        + "\"enumvsArray\":\"Sunday\",\"mapvsArray\":"
                        + "{\"key1\":\"Sunday\",\"key2\":\"Monday\"}}";
        String rightObject =
                "{\"daysvsString\":\"Sunday\",\"allOneOf\":2,"
                        + "\"allOuterArray\":[2,\"February\"],"
                        + "\"enumvsArray\":\"Sunday\",\"mapvsArray\":"
                        + "{\"key1\":\"Sunday\",\"key2\":\"Monday\"}}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, false, false);
        assertTrue(actual);
    }

    @Test
    public void testIsJsonProperSubsetOfList1() throws IOException {
        String leftObject =
                "{\"daysvsString\":\"Sunday\",\"allOneOf\":2,"
                        + "\"allOuterArray\":[2,\"February\"],"
                        + "\"enumvsArray\":\"Sunday\",\"mapvsArray\":"
                        + "{\"key1\":\"Sunday\",\"key2\":\"Monday\"}}";
        String rightObject =
                "{\"daysvsString\":\"Sunday\",\"allOneOf\":2,"
                        + "\"allOuterArray\":[2,\"February\"],"
                        + "\"enumvsArray\":\"Sunday\",\"mapvsArray\":"
                        + "{\"key1\":\"Sunday\",\"key2\":\"Monday\"}}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, false, true);
        assertTrue(actual);
    }

    @Test
    public void testIsJsonProperSubsetOfList2() throws IOException {
        String leftObject =
                "{\"daysvsString\":\"Sunday\",\"allOneOf\":2,"
                        + "\"allOuterArray\":[2,\"February\"],"
                        + "\"enumvsArray\":\"Sunday\",\"mapvsArray\":"
                        + "{\"key1\":\"Sunday\",\"key2\":\"Monday\"}}";
        String rightObject =
                "{\"daysvsString\":\"Sunday\",\"allOneOf\":2,"
                        + "\"allOuterArray\":[2,\"February\"],"
                        + "\"enumvsArray\":\"Sunday\",\"mapvsArray\":"
                        + "{\"key1\":\"Sunday\",\"key2\":\"Monday\"}}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, true, true);
        assertTrue(actual);
    }

    @Test
    public void testIsJsonProperSubsetOfList3() throws IOException {
        String leftObject =
                "{\"daysvsString\":\"Sunday\",\"allOneOf\":2,"
                        + "\"allOuterArray\":[2,\"February\"],"
                        + "\"enumvsArray\":\"Sunday\",\"mapvsArray\":"
                        + "{\"key1\":\"Sunday\",\"key2\":\"Monday\"}}";
        String rightObject =
                "{\"daysvsString\":\"Sunday\",\"allOneOf\":2,"
                        + "\"allOuterArray\":[2,\"February\"],"
                        + "\"enumvsArray\":\"Sunday\",\"mapvsArray\":"
                        + "{\"key1\":\"Sunday\",\"key2\":\"Monday\"}}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, true, false);
        assertTrue(actual);
    }

    @Test
    public void testIsJsonProperSubsetOf1() throws IOException {
        String leftObject =
                "{\"multiAnyOf\":\"some string\",\"multiOneOfAnyOf\":\"some string\","
                        + "\"singleInnerMapOfArray\":{\"key1\":[23,23],\"key2\":[23,23]},"
                        + "\"outerMapOfSingleInnerArray\":{\"key1\":[23,23],\"key2\":"
                        + "{\"NumberOfElectrons\":4}},\"allInnerArrayOfMap\":"
                        + "[{\"key1\":false,\"key2\":true},{\"key1\":false,\"key2\":true}]"
                        + ",\"allInnerArrayOfMap2\":{\"key1\":[{\"key1\":false,\"key2\":true}"
                        + ",{\"key1\":false,\"key2\":true}],\"key2\":[{\"key1\":"
                        + "{\"NumberOfTyres\":\"4\"},\"key2\":{\"NumberOfTyres\":\"4\"}}]},"
                        + "\"outerArrayOfMap\":[{\"key1\":{\"NumberOfTyres\":\"4\","
                        + "\"HaveTrunk\":true},\"key2\":\"some string\"},{\"key1\":"
                        + "{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true},\"key2\":"
                        + "{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}}],"
                        + "\"outerArrayOfMap2\":[{\"key1\":[{\"NumberOfTyres\":\"4\","
                        + "\"HaveTrunk\":true},{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}],"
                        + "\"key2\":[\"some string\",\"some string\"]},{\"key1\":"
                        + "[{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}],\"key2\":"
                        + "[{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}]}],"
                        + "\"outerMapOfArray\":{\"key1\":[{\"name\":\"Shahid Khaliq\","
                        + "\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54+00:00\",\"personType\":\"Per\"},12.3],"
                        + "\"key2\":[12.3,12.3]},\"outerMapOfArray2\":{\"key1\":"
                        + "[{\"key1\":12.3,\"key2\":12.3},{\"key1\":{\"name\":\"Shahid Khaliq\","
                        + "\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54+00:00\",\"personType\":\"Per\"}}],"
                        + "\"key2\":[{\"key1\":12.3,\"key2\":12.3},{\"key1\":"
                        + "{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54+00:00\",\"personType\":\"Per\"},"
                        + "\"key2\":{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54+00:00\",\"personType\":"
                        + "\"Per\"}}]}}";
        String rightObject =
                "{\"multiAnyOf\":\"some string\",\"multiOneOfAnyOf\":"
                        + "\"some string\",\"singleInnerMapOfArray\":{\"key1\":[23,23],\"key2\":"
                        + "[23,23]},\"outerMapOfSingleInnerArray\":{\"key1\":[23,23],\"key2\":"
                        + "{\"NumberOfElectrons\":4}},\"allInnerArrayOfMap\":[{\"key1\":false,"
                        + "\"key2\":true},{\"key1\":false,\"key2\":true}],\"allInnerArrayOfMap2\":"
                        + "{\"key1\":[{\"key1\":false,\"key2\":true},{\"key1\":false,"
                        + "\"key2\":true}],\"key2\":[{\"key1\":{\"NumberOfTyres\":\"4\"},\"key2\":"
                        + "{\"NumberOfTyres\":\"4\"}}]},\"outerArrayOfMap\":[{\"key1\":"
                        + "{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"},\"key2\":\"some string\"},"
                        + "{\"key1\":{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"},\"key2\":"
                        + "{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"}}],\"outerArrayOfMap2\":"
                        + "[{\"key1\":[{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"},"
                        + "{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"}],\"key2\":"
                        + "[\"some string\",\"some string\"]},{\"key1\":[{\"HaveTrunk\":true,"
                        + "\"NumberOfTyres\":\"4\"}],\"key2\":[{\"HaveTrunk\":true,"
                        + "\"NumberOfTyres\":\"4\"}]}],\"outerMapOfArray\":{\"key1\":"
                        + "[{\"address\":\"H # 531, S # 20\",\"age\":5147483645,"
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54+00:"
                        + "00\",\"name\":\"Shahid Khaliq\",\"uid\":\"123321\",\"personType\":"
                        + "\"Per\"},12.3],\"key2\":[12.3,12.3]},\"outerMapOfArray2\":{\"key1\":"
                        + "[{\"key1\":12.3,\"key2\":12.3},{\"key1\":{\"address\":\"H # 531, "
                        + "S # 20\",\"age\":5147483645,\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54+00:00\",\"name\":"
                        + "\"Shahid Khaliq\",\"uid\":\"123321\",\"personType\":\"Per\"}}],"
                        + "\"key2\":[{\"key1\":12.3,\"key2\":12.3},{\"key1\":{\"address\":"
                        + "\"H # 531, S # 20\",\"age\":5147483645,\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54+00:00\",\"name\":\"Shahid Khaliq\","
                        + "\"uid\":\"123321\",\"personType\":\"Per\"},\"key2\":{\"address\":"
                        + "\"H # 531, S # 20\",\"age\":5147483645,\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54+00:00\",\"name\":\"Shahid Khaliq\""
                        + ",\"uid\":\"123321\",\"personType\":\"Per\"}}]}}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, true, false);
        assertTrue(actual);
    }

    @Test
    public void testIsJsonProperSubsetOf2() throws IOException {
        String leftObject =
                "{\"multiAnyOf\":\"some string\",\"multiOneOfAnyOf\":\"some string\","
                        + "\"singleInnerMapOfArray\":{\"key1\":[23,23],\"key2\":[23,23]},"
                        + "\"outerMapOfSingleInnerArray\":{\"key1\":[23,23],\"key2\":"
                        + "{\"NumberOfElectrons\":4}},\"allInnerArrayOfMap\":"
                        + "[{\"key1\":false,\"key2\":true},{\"key1\":false,\"key2\":true}]"
                        + ",\"allInnerArrayOfMap2\":{\"key1\":[{\"key1\":false,\"key2\":true}"
                        + ",{\"key1\":false,\"key2\":true}],\"key2\":[{\"key1\":"
                        + "{\"NumberOfTyres\":\"4\"},\"key2\":{\"NumberOfTyres\":\"4\"}}]},"
                        + "\"outerArrayOfMap\":[{\"key1\":{\"NumberOfTyres\":\"4\","
                        + "\"HaveTrunk\":true},\"key2\":\"some string\"},{\"key1\":"
                        + "{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true},\"key2\":"
                        + "{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}}],"
                        + "\"outerArrayOfMap2\":[{\"key1\":[{\"NumberOfTyres\":\"4\","
                        + "\"HaveTrunk\":true},{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}],"
                        + "\"key2\":[\"some string\",\"some string\"]},{\"key1\":"
                        + "[{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}],\"key2\":"
                        + "[{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}]}],"
                        + "\"outerMapOfArray\":{\"key1\":[{\"name\":\"Shahid Khaliq\","
                        + "\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54+00:00\",\"personType\":\"Per\"},12.3],"
                        + "\"key2\":[12.3,12.3]},\"outerMapOfArray2\":{\"key1\":"
                        + "[{\"key1\":12.3,\"key2\":12.3},{\"key1\":{\"name\":\"Shahid Khaliq\","
                        + "\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54+00:00\",\"personType\":\"Per\"}}],"
                        + "\"key2\":[{\"key1\":12.3,\"key2\":12.3},{\"key1\":"
                        + "{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54+00:00\",\"personType\":\"Per\"},"
                        + "\"key2\":{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54+00:00\",\"personType\":"
                        + "\"Per\"}}]}}";
        String rightObject =
                "{\"multiAnyOf\":{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"},"
                        + "\"multiOneOfAnyOf\":{\"HaveTrunk\":true,"
                        + "\"NumberOfTyres\":\"4\"},\"singleInnerMapOfArray\":"
                        + "{\"key1\":[{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}"
                        + ",{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}],\"key2\":"
                        + "[{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}]},"
                        + "\"outerMapOfSingleInnerArray\":{\"key1\":[{\"NumberOfElectrons\":"
                        + "4,\"NumberOfProtons\":4},{\"NumberOfElectrons\":4,"
                        + "\"NumberOfProtons\":4}],\"key2\":{\"NumberOfElectrons\":4,"
                        + "\"NumberOfProtons\":4}},\"allInnerArrayOfMap\":[{\"key1\":"
                        + "{\"NumberOfElectrons\":4},\"key2\":{\"NumberOfElectrons\":4}},"
                        + "{\"key1\":{\"NumberOfElectrons\":4},\"key2\":{"
                        + "\"NumberOfElectrons\":4}}],\"allInnerArrayOfMap2\":{\"key1\":"
                        + "[{\"key1\":{\"NumberOfElectrons\":4},\"key2\":{\"NumberOfElectrons\":"
                        + "4}},{\"key1\":{\"NumberOfElectrons\":4},\"key2\":"
                        + "{\"NumberOfElectrons\":"
                        + "4}}],\"key2\":[{\"key1\":{\"NumberOfTyres\":\"4\"},\"key2\":"
                        + "{\"NumberOfTyres\":\"4\"}}]},\"outerArrayOfMap\":[{\"key1\":"
                        + "{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"},\"key2\":"
                        + "{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}},{\"key1\":"
                        + "{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"},\"key2\":"
                        + "{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"}}],"
                        + "\"outerArrayOfMap2\":[{\"key1\":[{\"HaveTrunk\":true,"
                        + "\"NumberOfTyres\":\"4\"},{\"HaveTrunk\":true,\"NumberOfTyres\":"
                        + "\"4\"}],\"key2\":[{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}"
                        + ",{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}]},{\"key1\":"
                        + "[{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"}],\"key2\":"
                        + "[{\"HaveTrunk\":true,\"NumberOfTyres\":\"4\"},{\"HaveTrunk\":"
                        + "true,\"NumberOfTyres\":\"4\"}]}],\"outerMapOfArray\":{\"key1\":"
                        + "[{\"startsAt\":\"6:00\",\"endsAt\":\"11:00\",\"offerTeaBreak\":"
                        + "true,\"sessionType\":\"Morning\"},{\"startsAt\":\"6:00\","
                        + "\"endsAt\":\"11:00\",\"offerDinner\":true,\"sessionType\":"
                        + "\"Evening\"}],\"key2\":[{\"startsAt\":\"6:00\",\"endsAt\":"
                        + "\"11:00\",\"offerLunch\":true,\"sessionType\":\"Noon\"},"
                        + "{\"startsAt\":\"6:00\",\"endsAt\":\"11:00\",\"offerDinner\":"
                        + "true,\"sessionType\":\"Evening\"}]},\"outerMapOfArray2\":"
                        + "{\"key1\":[{\"key1\":{\"startsAt\":\"6:00\",\"endsAt\":"
                        + "\"11:00\",\"offerLunch\":true,\"sessionType\":\"Noon\"},"
                        + "\"key2\":{\"startsAt\":\"6:00\",\"endsAt\":\"11:00\","
                        + "\"offerLunch\":true,\"sessionType\":\"Noon\"}},"
                        + "{\"key1\":{\"startsAt\":\"6:00\",\"endsAt\":\"11:00\","
                        + "\"offerTeaBreak\":true,\"sessionType\":\"Morning\"}}],"
                        + "\"key2\":[{\"key1\":{\"startsAt\":\"6:00\",\"endsAt\":"
                        + "\"11:00\",\"offerDinner\":true,\"sessionType\":\"Evening\"}},"
                        + "{\"key1\":{\"startsAt\":\"6:00\",\"endsAt\":\"11:00\","
                        + "\"offerTeaBreak\":true,\"sessionType\":\"Morning\"},\"key2\":"
                        + "{\"startsAt\":\"6:00\",\"endsAt\":\"11:00\",\"offerTeaBreak\":"
                        + "true,\"sessionType\":\"Morning\"}}]}}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, true, false);
        assertFalse(actual);
    }


    @Test
    public void testIsJsonObjectProperSubsetOf() throws IOException {
        String leftObject =
                "{\"anyOfRequired\":{\"NumberOfElectrons\":2},\"oneOfReqNullable\":"
                        + "{\"NumberOfTyres\":\"2\"},\"anyOfOptNullable\":null}";
        String rightObject =
                "{\"anyOfRequired\":{\"NumberOfElectrons\":2},\"oneOfReqNullable\":"
                        + "{\"NumberOfTyres\":\"2\"},\"anyOfOptNullable\":\"test\"}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, false, false);
        assertFalse(actual);
    }

    @Test
    public void testIsJsonObjectProperSubsetOfList() throws IOException {
        String leftObject =
                "{\"multiAnyOf\":23,\"multiOneOfAnyOf\":23,\"singleInnerMapOfArray\":"
                        + "{\"key1\":[12.3,12.3],\"key2\":[12.3,12.3]},"
                        + "\"outerMapOfSingleInnerArray\":"
                        + "{\"key1\":[12.3,12.3],\"key2\":12.3},\"allInnerArrayOfMap\":"
                        + "[{\"key1\":12,\"key2\":12},{\"key1\":12,\"key2\":12}],"
                        + "\"allInnerArrayOfMap2\":{\"key1\":[{\"key1\":12,\"key2\":12},"
                        + "{\"key1\":12,\"key2\":12}],\"key2\":[{\"key1\":\"some string\","
                        + "\"key2\":\"some string\"}]},\"outerArrayOfMap\":[{\"key1\":12,"
                        + "\"key2\":\"some string\"},{\"key1\":12,\"key2\":12}],"
                        + "\"outerArrayOfMap2\":[{\"key1\":[12,12],\"key2\":"
                        + "[\"some string\",\"some string\"]},{\"key1\":[12,12],"
                        + "\"key2\":[12,12]}],\"outerMapOfArray\":{\"key1\":"
                        + "[12,true],\"key2\":[false,true]},\"outerMapOfArray2\":"
                        + "{\"key1\":[{\"key1\":12,\"key2\":12},{\"key1\":true,"
                        + "\"key2\":false}],\"key2\":[{\"key1\":12,\"key2\":12},"
                        + "{\"key1\":12,\"key2\":12}]}}";
        String rightObject =
                "{\"multiAnyOf\":23,\"multiOneOfAnyOf\":21,\"singleInnerMapOfArray\":"
                        + "{\"key1\":[12.3,12.3],\"key2\":[12.3,12.3]},"
                        + "\"outerMapOfSingleInnerArray\":{\"key1\":[12.3,12.3],"
                        + "\"key2\":12.3},\"allInnerArrayOfMap\":[{\"key1\":12,"
                        + "\"key2\":12},{\"key1\":12,\"key2\":12}],\"allInnerArrayOfMap2\":"
                        + "{\"key1\":[{\"key1\":12,\"key2\":12},{\"key1\":12,\"key2\":16}],"
                        + "\"key2\":[{\"key1\":\"some string\",\"key2\":\"some string\"}]},"
                        + "\"outerArrayOfMap\":[{\"key1\":12,\"key2\":\"some string\"},"
                        + "{\"key1\":12,\"key2\":12}],\"outerArrayOfMap2\":[{\"key1\":[12,12],"
                        + "\"key2\":[\"some string\",\"some \"]},{\"key1\":[12,12],"
                        + "\"key2\":[12,12]}],"
                        + "\"outerMapOfArray\":{\"key1\":[12,true],\"key2\":[false,true]},"
                        + "\"outerMapOfArray2\":{\"key1\":[{\"key1\":12,\"key2\":12},"
                        + "{\"key1\":true,\"key2\":false}],\"key2\":[{\"key1\":12,\"key2\""
                        + ":\"some string\"},{\"key1\":12,\"key2\":12}]}}";
        boolean actual =
                TestHelper.isJsonObjectProperSubsetOf(leftObject, rightObject, true, true, false);
        assertFalse(actual);
    }

    @Test
    public void testIsArrayOfJsonObjectsProperSubsetOf() throws IOException {
        String leftObject =
                "[{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":\"H # 531, S # 20\","
                        + "\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],"
                        + "\"boss\":{\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":"
                        + "5147483645,\"address\":\"H # 531, S # 20\",\"uid\":\"123321\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54."
                        + "9571247Z\",\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],"
                        + "\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54."
                        + "9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,\"address\""
                        + ":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":1484719381},"
                        + "\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"name\":\"Shahid Khaliq\","
                        + "\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\",\"workingDays\""
                        + ":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":{\"personType\":"
                        + "\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},{\"name\":"
                        + "\"Future Kid\",\"age\":5147483648,\"address\":\"H # 531, S # 20\","
                        + "\"uid\":\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":1484719381},"
                        + "\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"312341\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"}]";
        String rightObject =
                "[{\"personType\":\"Empl\",\"name\":\"Shahid Khaliq\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":"
                        + "{\"personType\":"
                        + "\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},{\"name\":"
                        + "\"Future Kid\","
                        + "\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":\"312341\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:"
                        + "54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":"
                        + "1484719381},\"dependents\":[{\"name\":\"Future Wife\",\"age\":"
                        + "5147483649,\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:"
                        + "54.9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"312341\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"personType\":"
                        + "\"Empl\",\"name\":\"Shahid Khaliq\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":"
                        + "\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\","
                        + "\"Friday\"],\"boss\":{\"personType\":\"Boss\",\"name\":"
                        + "\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"dependents\":[{\"name\":"
                        + "\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}"
                        + ",{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":"
                        + "1484719381},\"dependents\":[{\"name\":\"Future Wife\",\"age\""
                        + ":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54"
                        + ".9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\""
                        + ":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}]"
                        + ",\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\"}]";
        boolean actual =
                TestHelper.isArrayOfJsonObjectsProperSubsetOf(leftObject, rightObject, true, true,
                        false);
        assertTrue(actual);
    }

    @Test
    public void testIsArrayOfJsonObjectsProperSubsetOf1() throws IOException {
        String leftObject =
                "[{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":\"H # 531, S # 20\","
                        + "\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],"
                        + "\"boss\":{\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":"
                        + "5147483645,\"address\":\"H # 531, S # 20\",\"uid\":\"123321\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54."
                        + "9571247Z\",\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\","
                        + "\"Friday\"],\"dependents\":[{\"name\":\"Future Wife\","
                        + "\"age\":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":"
                        + "\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123412\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"312341\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"name\":"
                        + "\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"boss\":{\"personType\":\"Boss\","
                        + "\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"dependents\":[{\"name\":"
                        + "\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":[{\"name\":"
                        + "\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}"
                        + ",{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\""
                        + ",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"}]";
        String rightObject =
                "[{\"personType\":\"Empl\",\"name\":\"Shahid Khaliq\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\","
                        + "\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\""
                        + ",\"Friday\"],\"boss\":{\"personType\":\"Boss\",\"name\":"
                        + "\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":\"H # 531, "
                        + "S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\","
                        + "\"uid\":\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\","
                        + "\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":1484719381},"
                        + "\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"personType\":"
                        + "\"Empl\",\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":"
                        + "{\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":"
                        + "\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}]"
                        + ",\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":[{"
                        + "\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\","
                        + "\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"}]";
        boolean actual =
                TestHelper.isArrayOfJsonObjectsProperSubsetOf(leftObject, rightObject, false, false,
                        false);
        assertTrue(actual);
    }

    @Test
    public void testIsArrayOfJsonObjectsProperSubsetOf2() throws IOException {
        String leftObject =
                "[{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":\"H # 531, S # 20\","
                        + "\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],"
                        + "\"boss\":{\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":"
                        + "5147483645,\"address\":\"H # 531, S # 20\",\"uid\":\"123321\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54."
                        + "9571247Z\",\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\","
                        + "\"Friday\"],\"dependents\":[{\"name\":\"Future Wife\","
                        + "\"age\":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":"
                        + "\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123412\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"312341\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"name\":"
                        + "\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"boss\":{\"personType\":\"Boss\","
                        + "\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"dependents\":[{\"name\":"
                        + "\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":[{\"name\":"
                        + "\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}"
                        + ",{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\""
                        + ",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"}]";
        String rightObject =
                "[{\"personType\":\"Empl\",\"name\":\"Shahid Khaliq\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\","
                        + "\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\""
                        + ",\"Friday\"],\"boss\":{\"personType\":\"Boss\",\"name\":"
                        + "\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":\"H # 531, "
                        + "S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\","
                        + "\"uid\":\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\","
                        + "\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":1484719381},"
                        + "\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"personType\":"
                        + "\"Empl\",\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":"
                        + "{\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":"
                        + "\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}]"
                        + ",\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":[{"
                        + "\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\","
                        + "\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"}]";
        boolean actual =
                TestHelper.isArrayOfJsonObjectsProperSubsetOf(leftObject, rightObject, false, false,
                        true);
        assertTrue(actual);
    }

    @Test
    public void testIsArrayOfJsonObjectsProperSubsetOf3() throws IOException {
        String leftObject =
                "[{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":\"H # 531, S # 20\","
                        + "\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],"
                        + "\"boss\":{\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":"
                        + "5147483645,\"address\":\"H # 531, S # 20\",\"uid\":\"123321\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54."
                        + "9571247Z\",\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\","
                        + "\"Friday\"],\"dependents\":[{\"name\":\"Future Wife\","
                        + "\"age\":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":"
                        + "\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123412\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"312341\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"name\":"
                        + "\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"boss\":{\"personType\":\"Boss\","
                        + "\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"dependents\":[{\"name\":"
                        + "\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":[{\"name\":"
                        + "\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}"
                        + ",{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\""
                        + ",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"}]";
        String rightObject =
                "[{\"personType\":\"Empl\",\"name\":\"Shahid Khaliq\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\","
                        + "\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\""
                        + ",\"Friday\"],\"boss\":{\"personType\":\"Boss\",\"name\":"
                        + "\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":\"H # 531, "
                        + "S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,\"address\":"
                        + "\"H # 531, S # 20\","
                        + "\"uid\":\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\","
                        + "\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":1484719381},"
                        + "\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},"
                        + "{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"personType\":"
                        + "\"Empl\",\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,"
                        + "\"department\":\"Software Development\",\"joiningDay\":\"Saturday\","
                        + "\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":"
                        + "{\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\","
                        + "\"salary\":20000,\"department\":\"Software Development\","
                        + "\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\","
                        + "\"Tuesday\",\"Friday\"],\"dependents\":"
                        + "[{\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":"
                        + "\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}]"
                        + ",\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":[{"
                        + "\"name\":\"Future Wife\",\"age\":5147483649,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\","
                        + "\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"}]";
        boolean actual =
                TestHelper.isArrayOfJsonObjectsProperSubsetOf(leftObject, rightObject, false, true,
                        true);
        assertTrue(actual);
    }


    @Test
    public void testAreHeadersProperSubsetOf() {
        Map<String, String> leftTree = new LinkedHashMap<>();
        leftTree.put("NauManAl", "b9cb2f80-1b64-43ee-a6da-71f7ef686fa9");
        leftTree.put("WaseemHasAn", "b9cb2f80-1b64-43ee-a6da-71f7ef686fa9");

        Map<String, String> rightTree = new LinkedHashMap<>();
        rightTree.put("naumanali", "enh, not so much");
        rightTree.put("WaseemHasAn", "is also awesome");
        rightTree.put("date", "Tue, 06 Sep 2022 09:41:49 GMT");
        rightTree.put("content-length", "0");

        boolean actual = TestHelper.areHeadersProperSubsetOf(leftTree, rightTree, true);
        assertFalse(actual);
    }

    @Test
    public void testAreHeadersProperSubsetOf1() {
        Map<String, String> leftTree = new LinkedHashMap<>();
        leftTree.put("NauManAl", "b9cb2f80-1b64-43ee-a6da-71f7ef686fa9");
        leftTree.put("WaseemHasAn", "b9cb2f80-1b64-43ee-a6da-71f7ef686fa9");

        Map<String, String> rightTree = new LinkedHashMap<>();
        rightTree.put("NauManAl", "enh, not so much");
        rightTree.put("WaseemHasAn", "is also awesome");
        rightTree.put("date", "Tue, 06 Sep 2022 09:41:49 GMT");
        rightTree.put("content-length", "0");

        boolean actual = TestHelper.areHeadersProperSubsetOf(leftTree, rightTree, false);
        assertTrue(actual);
    }


    @Test
    public void testIsProperSubsetOf() throws IOException {
        String leftObject =
                "{\"key1\":[{\"NumberOfElectrons\":4,\"NumberOfProtons\":4},"
                        + "{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}],\"key2\":"
                        + "{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}}";
        String rightObject =
                "{\"key1\":[{\"NumberOfElectrons\":4,\"NumberOfProtons\":4},"
                        + "{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}],"
                        + "\"key2\":{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}}";
        boolean actual = TestHelper.isProperSubsetOf(leftObject, rightObject, true, true, false);
        assertTrue(actual);
    }

    @Test
    public void testIsProperSubsetOfList() throws IOException {
        String leftObject = "[0.987,0.987]";
        String rightObject = "[0.987,0.987]";
        boolean actual = TestHelper.isProperSubsetOf(leftObject, rightObject, true, true, false);
        assertTrue(actual);
    }

    @Test
    public void testIsProperSubsetOfList1() throws IOException {
        String leftObject = "[0.987,0.987]";
        String rightObject = "[0.987,0.987,0.578]";
        boolean actual = TestHelper.isProperSubsetOf(leftObject, rightObject, true, false, false);
        assertFalse(actual);
    }

    @Test
    public void testIsProperSubsetOfArrayOfArray() throws IOException {
        String leftObject = "[[{\"key1\": \"value\", \"key2\":\"value2\", \"key2\":\"value3\"}]]";
        String rightObject = "[[{\"key1\": \"value\", \"key2\":\"value2\", \"key2\":\"value3\"}]]";
        boolean actual = TestHelper.isProperSubsetOf(leftObject, rightObject, true, true, false);
        assertTrue(actual);
    }

    @Test
    public void testIsProperSubsetOf1() throws IOException {
        String leftObject =
                "{\"key1\":[{\"NumberOfElectrons\":4,\"NumberOfProtons\":4},"
                        + "{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}],"
                        + "\"key2\":{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}}";
        String rightObject =
                "{\"key1\":[{\"NumberOfElectrons\":4,\"NumberOfProtons\":4},"
                        + "{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}],"
                        + "\"key2\":{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}}";
        boolean actual = TestHelper.isProperSubsetOf(leftObject, rightObject, true, true, true);
        assertTrue(actual);
    }


    @Test
    public void testIsProperSubsetOf2() throws IOException {
        String leftObject =
                "{\"key1\":[{\"NumberOfElectrons\":4,\"NumberOfProtons\":4},"
                        + "{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}],"
                        + "\"key2\":{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}}";
        String rightObject =
                "{\"key1\":[{\"NumberOfElectrons\":2,\"NumberOfProtons\":4},"
                        + "{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}],"
                        + "\"key2\":{\"NumberOfElectrons\":4,\"NumberOfProtons\":4}}";
        boolean actual = TestHelper.isProperSubsetOf(leftObject, rightObject, true, true, true);
        assertFalse(actual);
    }

    @Test
    public void testIsProperSubsetOfArray() throws IOException {
        String leftObject = "[{\"key1\":12,\"key2\":\"some string\"},{\"key1\":12,\"key2\":12}]";
        String rightObject = "[{\"key1\":12,\"key2\":\"some string\"},{\"key1\":12,\"key2\":12}]";
        boolean actual = TestHelper.isProperSubsetOf(leftObject, rightObject, true, true, true);
        assertTrue(actual);
    }

    @Test
    public void testPrepareForComparison() throws JsonProcessingException, IOException {
        String json = "[{\"key1\":12,\"key2\":\"some string\"},{\"key1\":12,\"key2\":12}]";
        String actual = TestHelper.prepareForComparison(json, new TypeReference<Object>() {});
        assertEquals(actual, json);
    }

    @Test
    public void testConvertInputStreamToString() {
        String stringToCOnvert = "test string";
        InputStream inputStream = new ByteArrayInputStream(stringToCOnvert.getBytes());
        String expected = "test string";
        String actual = TestHelper.convertStreamToString(inputStream);
        assertEquals(actual, expected);
    }

    @Test
    public void testContainsBigDecimal() {
        List<BigDecimal> expected = new ArrayList<>();
        expected.add(new BigDecimal(BIG_NUMBER1));
        List<BigDecimal> actual = new ArrayList<>();
        actual.add(new BigDecimal(BIG_NUMBER1));
        boolean result = TestHelper.containsBigDecimalList(actual, expected);
        assertTrue(result);
    }

    @Test
    public void testContainsBigDecimal1() {
        List<BigDecimal> expected = new ArrayList<>();
        expected.add(new BigDecimal(BIG_NUMBER1));
        List<BigDecimal> actual = new ArrayList<>();
        actual.add(new BigDecimal(BIG_NUMBER4));
        boolean result = TestHelper.containsBigDecimalList(actual, expected);
        assertFalse(result);
    }

    @Test
    public void testEqualsBigDecimalList() {
        List<BigDecimal> expected = new ArrayList<>();
        expected.add(new BigDecimal(BIG_NUMBER1));
        List<BigDecimal> actual = new ArrayList<>();
        actual.add(new BigDecimal(BIG_NUMBER1));
        boolean result = TestHelper.equalsBigDecimalList(actual, expected);
        assertTrue(result);
    }

    @Test
    public void testEqualsBigDecimalList1() {
        List<BigDecimal> expected = new ArrayList<>();
        expected.add(new BigDecimal(BIG_NUMBER1));
        List<BigDecimal> actual = new ArrayList<>();
        actual.add(new BigDecimal(BIG_NUMBER3));
        boolean result = TestHelper.equalsBigDecimalList(actual, expected);
        assertFalse(result);
    }

    @Test
    public void testEqualsBigDecimalList2() {
        List<BigDecimal> expected = new ArrayList<>();
        expected.add(new BigDecimal(BIG_NUMBER1));
        expected.add(new BigDecimal(BIG_NUMBER2));
        List<BigDecimal> actual = new ArrayList<>();
        actual.add(new BigDecimal(BIG_NUMBER3));
        boolean result = TestHelper.equalsBigDecimalList(actual, expected);
        assertFalse(result);
    }

    @Test
    public void testGetFile() throws IOException {
        File file = TestHelper.getFile(TEST_FILE_PATH);
        assertNotNull(file);
    }

    @Test
    public void testIsSameAsInput() throws IOException {
        String fileContent = "This test file is created to test CoreFileWrapper functionality";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        assertTrue(TestHelper.isSameAsFile(TEST_FILE_PATH, inputStream));
    }

    @Test
    public void testIsSameAsInput1() throws IOException {
        String fileContent = "This test file is created to test CoreFileWrapper functionality";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        assertTrue(TestHelper.isSameAsFile(TEST_FILE_PATH, bufferedInputStream));
    }

    @Test
    public void testIsSameAsInput3() throws IOException {
        File file = TestHelper.getFile(TEST_FILE_PATH);
        assertTrue(TestHelper.isSameAsFile(TEST_FILE_PATH, new FileInputStream(file)));
    }
}


