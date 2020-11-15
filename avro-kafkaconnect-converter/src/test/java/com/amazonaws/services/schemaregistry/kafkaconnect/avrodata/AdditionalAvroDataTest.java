/*
 * Copyright 2019 Confluent Inc.
 * Portions Copyright 2020 Amazon.com, Inc. or its affiliates.
 * All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amazonaws.services.schemaregistry.kafkaconnect.avrodata;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import io.test.avro.core.AvroMessage;
import io.test.avro.doc.DocTestRecord;
import io.test.avro.union.FirstOption;
import io.test.avro.union.MultiTypeUnionMessage;
import io.test.avro.union.SecondOption;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.Union;
import org.apache.avro.specific.SpecificData;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class AdditionalAvroDataTest
{
    private AvroData avroData;
    @Before
    public void before(){
        AvroDataConfig avroDataConfig = new AvroDataConfig.Builder()
                .with(AvroDataConfig.SCHEMAS_CACHE_SIZE_CONFIG, 1)
                .with(AvroDataConfig.CONNECT_META_DATA_CONFIG, false)
                .with(AvroDataConfig.ENHANCED_AVRO_SCHEMA_SUPPORT_CONFIG, true)
                .build();

        avroData = new AvroData(avroDataConfig);
    }


    @Test
    public void testDocumentationPreservedSchema() throws IOException
    {
        Schema avroSchema = new Parser().parse(new File("src/test/avro/DocTestRecord.avsc"));

        org.apache.kafka.connect.data.Schema connectSchema = avroData.toConnectSchema(avroSchema);

        Schema outputAvroSchema = avroData.fromConnectSchema(connectSchema);

        Assert.assertEquals(avroSchema, outputAvroSchema);
    }

    @Test
    public void testDocumentationPreservedData() throws IOException
    {
        PodamFactory factory = new PodamFactoryImpl();

        DocTestRecord testRecord = factory.manufacturePojo(DocTestRecord.class);
        org.apache.kafka.connect.data.SchemaAndValue connectSchemaAndValue = avroData.toConnectData(testRecord.getSchema(), testRecord);

        Object output = avroData.fromConnectData(connectSchemaAndValue.schema(), connectSchemaAndValue.value());

        Assert.assertEquals(SpecificData.get().toString(testRecord), SpecificData.get().toString(output));
    }

    @Test
    public void testComplexUnionSchema() throws IOException
    {
        // Here is a schema complex union schema
        Schema avroSchema = new Parser().parse(new File("src/test/avro/AvroMessage.avsc"));

        org.apache.kafka.connect.data.Schema connectSchema = avroData.toConnectSchema(avroSchema);

        Schema outputAvroSchema = avroData.fromConnectSchema(connectSchema);

        Assert.assertEquals(avroSchema, outputAvroSchema);
    }

    @Test
    public void testComplexUnionData() throws IOException
    {
        PodamFactory factory = new PodamFactoryImpl();

        AvroMessage avroMessage = factory.manufacturePojo(AvroMessage.class);
        org.apache.kafka.connect.data.SchemaAndValue connectSchemaAndValue = avroData.toConnectData(avroMessage.getSchema(), avroMessage);

        Object output = avroData.fromConnectData(connectSchemaAndValue.schema(), connectSchemaAndValue.value());

        Assert.assertEquals(SpecificData.get().toString(avroMessage), SpecificData.get().toString(output));
    }


    @Test
    public void testComplexMultiUnionData() throws IOException
    {
        PodamFactory factory = new PodamFactoryImpl();

        MultiTypeUnionMessage avroMessage = factory.manufacturePojo(MultiTypeUnionMessage.class);

        org.apache.kafka.connect.data.SchemaAndValue connectSchemaAndValue = avroData.toConnectData(avroMessage.getSchema(), avroMessage);
        Object output = avroData.fromConnectData(connectSchemaAndValue.schema(), connectSchemaAndValue.value());
        Assert.assertEquals(SpecificData.get().toString(avroMessage), SpecificData.get().toString(output));

        avroMessage.setCompositeRecord(new FirstOption("x",2l));
        connectSchemaAndValue = avroData.toConnectData(avroMessage.getSchema(), avroMessage);
        output = avroData.fromConnectData(connectSchemaAndValue.schema(), connectSchemaAndValue.value());
        Assert.assertEquals(SpecificData.get().toString(avroMessage), SpecificData.get().toString(output));

        avroMessage.setCompositeRecord(new SecondOption("y",3l));
        connectSchemaAndValue = avroData.toConnectData(avroMessage.getSchema(), avroMessage);
        output = avroData.fromConnectData(connectSchemaAndValue.schema(), connectSchemaAndValue.value());
        Assert.assertEquals(SpecificData.get().toString(avroMessage), SpecificData.get().toString(output));

        avroMessage.setCompositeRecord(Arrays.asList(new String[]{"1","2"}));
        connectSchemaAndValue = avroData.toConnectData(avroMessage.getSchema(), avroMessage);
        output = avroData.fromConnectData(connectSchemaAndValue.schema(), connectSchemaAndValue.value());
        Assert.assertEquals(SpecificData.get().toString(avroMessage), SpecificData.get().toString(output));

    }

    @Test
    public void testNestedUnion() {
        // Cannot use AllowNull to generate schema
        // because Avro 1.7.7 will throw org.apache.avro.AvroRuntimeException: Nested union
        // Schema myAvroObjectSchema = AllowNull.get().getSchema(MyObjectToPersist.class);

        // Here is a schema generated by Avro 1.8.1
        Schema myAvroObjectSchema = new Parser().parse(
                "{"
                        + "  \"type\" : \"record\","
                        + "  \"name\" : \"MyObjectToPersist\","
                        + "  \"namespace\" : \"com.amazonaws.services.schemaregistry.kafkaconnect.avrodata.AdditionalAvroDataTest\","
                        + "  \"fields\" : [ {"
                        + "    \"name\" : \"obj\","
                        + "    \"type\" : [ \"null\", {"
                        + "      \"type\" : \"record\","
                        + "      \"name\" : \"MyImpl1\","
                        + "      \"fields\" : [ {"
                        + "        \"name\" : \"data\","
                        + "        \"type\" : [ \"null\", \"string\" ],"
                        + "        \"default\" : null"
                        + "      } ]"
                        + "    }, {"
                        + "      \"type\" : \"record\","
                        + "      \"name\" : \"MyImpl2\","
                        + "      \"fields\" : [ {"
                        + "        \"name\" : \"data\","
                        + "        \"type\" : [ \"null\", \"string\" ],"
                        + "        \"default\" : null"
                        + "      } ]"
                        + "    } ],"
                        + "    \"default\" : null"
                        + "  } ]"
                        + "}");
        Schema myImpl1Schema = ReflectData.AllowNull.get().getSchema(MyImpl1.class);
        GenericData.Record nestedRecord = new GenericRecordBuilder(myImpl1Schema).set("data", "mydata").build();
        GenericData.Record obj = new GenericRecordBuilder(myAvroObjectSchema).set("obj", nestedRecord).build();

        org.apache.kafka.connect.data.Schema connectSchema = avroData.toConnectSchema(myAvroObjectSchema);
        SchemaAndValue schemaAndValue = avroData.toConnectData(myAvroObjectSchema, obj);
        Object o = avroData.fromConnectData(schemaAndValue.schema(), schemaAndValue.value());
        Assert.assertEquals(obj ,o);
        avroData.fromConnectSchema(connectSchema);
    }

    @Union({MyImpl1.class, MyImpl2.class})
    interface MyInterface {}
    static class MyImpl1 implements MyInterface
    { private String data; }
    static class MyImpl2 implements MyInterface
    { private String data; }
    static class MyObjectToPersist { private MyInterface obj; }

    @Test
    public void testRecordDefault() {
        Schema myAvroObjectSchema = new Parser().parse(
                "{"
                        + "  \"type\" : \"record\","
                        + "  \"name\" : \"MyRecord\","
                        + "  \"namespace\" : \"com.amazonaws.services.schemaregistry.kafkaconnect.avrodata.AdditionalAvroDataTest\","
                        + "  \"fields\" : [ {"
                        + "    \"name\" : \"obj\","
                        + "    \"type\" : {"
                        + "      \"name\" : \"obj2\","
                        + "      \"type\" : \"record\","
                        + "      \"fields\" : [ {"
                        + "        \"name\" : \"data\","
                        + "        \"type\" : \"string\","
                        + "        \"default\" : \"\""
                        + "      } ]"
                        + "    },"
                        + "    \"default\" : { \"data\" : \"\" }"
                        + "  } ]"
                        + "}");
        GenericData.Record obj = new GenericRecordBuilder(myAvroObjectSchema).build();

        org.apache.kafka.connect.data.Schema connectSchema = avroData.toConnectSchema(myAvroObjectSchema);
        SchemaAndValue schemaAndValue = avroData.toConnectData(myAvroObjectSchema, obj);
        Object o = avroData.fromConnectData(schemaAndValue.schema(), schemaAndValue.value());
        Assert.assertEquals(obj ,o);
        avroData.fromConnectSchema(connectSchema);
    }

    @Test
    public void testFieldRecordEnumDocumentationSchema() throws IOException
    {
        AvroDataConfig avroDataConfig = new AvroDataConfig.Builder()
                .with(AvroDataConfig.SCHEMAS_CACHE_SIZE_CONFIG, 1)
                .with(AvroDataConfig.CONNECT_META_DATA_CONFIG, true)
                .with(AvroDataConfig.ENHANCED_AVRO_SCHEMA_SUPPORT_CONFIG, true)
                .build();

        avroData = new AvroData(avroDataConfig);

        Schema avroSchema =
                new Parser().parse(new File("src/test/avro/RepeatedTypeWithDocFull.avsc"));

        Assert.assertEquals(avroSchema.getField("enumField").schema(),
                avroSchema.getField("anotherEnumField").schema());

        org.apache.kafka.connect.data.Schema connectSchema = avroData.toConnectSchema(avroSchema);

        Schema outputAvroSchema = avroData.fromConnectSchema(connectSchema);

        Assert.assertEquals("record's doc", outputAvroSchema.getDoc());
        Assert.assertEquals("field's doc",
                outputAvroSchema.getField("stringField").doc());
        Assert.assertNull(outputAvroSchema.getField("stringField").schema().getDoc());
        Assert.assertNull(outputAvroSchema.getField("anotherStringField").doc());
        Assert.assertNull(
                outputAvroSchema.getField("anotherStringField").schema().getDoc());

        Assert.assertEquals("record field's doc",
                outputAvroSchema.getField("recordField").doc());
        Assert.assertEquals("nested record's doc",
                outputAvroSchema.getField("recordField").schema().getDoc());
        Assert.assertEquals("nested record field's doc",
                outputAvroSchema.getField("recordField").schema()
                        .getField("nestedRecordField").doc());
        Assert.assertNull(outputAvroSchema.getField("recordField").schema()
                .getField("nestedRecordField").schema().getDoc());
        Assert.assertNull(outputAvroSchema.getField("recordField").schema()
                .getField("anotherNestedRecordField").doc());
        Assert.assertNull(outputAvroSchema.getField("recordField").schema()
                .getField("anotherNestedRecordField").schema().getDoc());

        Assert.assertEquals("another record field's doc",
                outputAvroSchema.getField("anotherRecordField").doc());
        Assert.assertEquals("nested record's doc",
                outputAvroSchema.getField("anotherRecordField").schema().getDoc());
        Assert.assertEquals("nested record field's doc",
                outputAvroSchema.getField("anotherRecordField").schema()
                        .getField("nestedRecordField").doc());
        Assert.assertNull(outputAvroSchema.getField("anotherRecordField").schema()
                .getField("nestedRecordField").schema().getDoc());
        Assert.assertNull(outputAvroSchema.getField("anotherRecordField").schema()
                .getField("anotherNestedRecordField").doc());
        Assert.assertNull(outputAvroSchema.getField("anotherRecordField").schema()
                .getField("anotherNestedRecordField").schema().getDoc());

        Assert.assertNull(outputAvroSchema.getField("recordFieldWithoutDoc").doc());

        Assert.assertNull(outputAvroSchema.getField("doclessRecordField").doc());
        Assert.assertNull(outputAvroSchema.getField("doclessRecordField").schema().getDoc());
        Assert.assertEquals("docless record field's doc",
                outputAvroSchema.getField("doclessRecordFieldWithDoc").doc());
        Assert.assertNull(outputAvroSchema.getField("doclessRecordFieldWithDoc").schema().getDoc());

        Assert.assertEquals("enum field's doc", outputAvroSchema.getField("enumField").doc());
        Assert.assertEquals("enum's doc", outputAvroSchema.getField("enumField").schema().getDoc());
        Assert.assertEquals("another enum field's doc",
                outputAvroSchema.getField("anotherEnumField").doc());
        Assert.assertEquals("enum's doc",
                outputAvroSchema.getField("anotherEnumField").schema().getDoc());

        Assert.assertNull(outputAvroSchema.getField("doclessEnumField").doc());
        Assert.assertNull(outputAvroSchema.getField("diffEnumField").doc());
        Assert.assertEquals("diffEnum's doc", outputAvroSchema.getField("diffEnumField").schema().getDoc());

        // Schema equality is mandatory (see issue #1042)
        Assert.assertEquals(outputAvroSchema.getField("stringField").schema(),
                outputAvroSchema.getField("anotherStringField").schema());
        Assert.assertEquals(outputAvroSchema.getField("recordField").schema(),
                outputAvroSchema.getField("anotherRecordField").schema());
        Assert.assertEquals(outputAvroSchema.getField("recordField").schema(),
                outputAvroSchema.getField("recordFieldWithoutDoc").schema());
        Assert.assertEquals(outputAvroSchema.getField("doclessRecordField").schema(),
                outputAvroSchema.getField("doclessRecordFieldWithDoc").schema());
        Assert.assertEquals(outputAvroSchema.getField("enumField").schema(),
                outputAvroSchema.getField("anotherEnumField").schema());
        Assert.assertEquals(outputAvroSchema.getField("enumField").schema(),
                outputAvroSchema.getField("doclessEnumField").schema());
    }

    @Test
    public void testRepeatedTypeWithDefault() throws IOException {
        AvroDataConfig avroDataConfig = new AvroDataConfig.Builder()
                .with(AvroDataConfig.SCHEMAS_CACHE_SIZE_CONFIG, 1)
                .with(AvroDataConfig.CONNECT_META_DATA_CONFIG, true)
                .with(AvroDataConfig.ENHANCED_AVRO_SCHEMA_SUPPORT_CONFIG, true)
                .build();

        avroData = new AvroData(avroDataConfig);

        Schema avroSchema =
                new Parser().parse(new File("src/test/avro/RepeatedTypeWithDefault.avsc"));

        org.apache.kafka.connect.data.Schema connectSchema = avroData.toConnectSchema(avroSchema);

        Schema outputAvroSchema = avroData.fromConnectSchema(connectSchema);

        Assert.assertEquals("field's default",
                outputAvroSchema.getField("stringField").defaultVal());
        Assert.assertEquals(null,
                outputAvroSchema.getField("anotherStringField").defaultVal());

        Assert.assertEquals("ONE", outputAvroSchema.getField("enumField").defaultVal());
        Assert.assertEquals("TWO", outputAvroSchema.getField("anotherEnumField").defaultVal());

        Assert.assertEquals("B", outputAvroSchema.getField("enumFieldWithDiffDefault").defaultVal());
        Assert.assertEquals("A", outputAvroSchema.getField("enumFieldWithDiffDefault").schema().getEnumDefault());

        Assert.assertEquals(9.18f, outputAvroSchema.getField("floatField").defaultVal());

        Assert.assertEquals(outputAvroSchema.getField("enumField").schema(),
                outputAvroSchema.getField("anotherEnumField").schema());
        Assert.assertEquals(outputAvroSchema.getField("stringField").schema(),
                outputAvroSchema.getField("anotherStringField").schema());
    }

}
