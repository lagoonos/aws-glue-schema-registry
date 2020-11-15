/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates.
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

package com.amazonaws.services.schemaregistry.flink.avro;

import com.amazonaws.services.schemaregistry.serializers.GlueSchemaRegistrySerializationFacade;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
import org.apache.avro.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlueSchemaRegistryAvroSerializationSchemaTest {
    @Mock
    private GlueSchemaRegistrySerializationFacade mockSerializationFacade;

    private static Schema userSchema;
    private static User userDefinedPojo;
    private static Map<String, Object> configs = new HashMap<>();
    private static Map<String, String> metadata = new HashMap<>();

    private static final String testTopic = "Test-Topic";
    private static final String schemaName = "User-Topic";
    private static final String AVRO_USER_SCHEMA_FILE = "src/test/java/resources/avro/user.avsc";
    private static final byte[] specificBytes = new byte[]{3, 0, -73, -76, -89, -16, -100, -106, 78, 74, -90, -121, -5,
            93, -23, -17, 12, 99, 8, 116, 101, 115, 116, 0, 20, 0, 12, 118, 105, 111, 108, 101, 116};

    @BeforeEach
    public void setup() throws IOException {
        metadata.put("test-key", "test-value");
        metadata.put(AWSSchemaRegistryConstants.TRANSPORT_METADATA_KEY, testTopic);

        configs.put(AWSSchemaRegistryConstants.AWS_REGION, "us-west-2");
        configs.put(AWSSchemaRegistryConstants.AWS_ENDPOINT, "https://test");
        configs.put(AWSSchemaRegistryConstants.SCHEMA_AUTO_REGISTRATION_SETTING, true);
        configs.put(AWSSchemaRegistryConstants.SCHEMA_NAME, schemaName);
        configs.put(AWSSchemaRegistryConstants.METADATA, metadata);

        Schema.Parser parser = new Schema.Parser();
        userSchema = parser.parse(new File(AVRO_USER_SCHEMA_FILE));
        userDefinedPojo = User.newBuilder().setName("test_avro_schema").setFavoriteColor("violet")
                .setFavoriteNumber(10).build();
    }

    /**
     * Test whether forGeneric method works
     */
    @Test
    public void testForGeneric_withValidParams_succeeds() {
        assertThat(GlueSchemaRegistryAvroSerializationSchema.forGeneric(userSchema, testTopic, configs), notNullValue());
        assertThat(GlueSchemaRegistryAvroSerializationSchema.forGeneric(userSchema, testTopic, configs),
                instanceOf(GlueSchemaRegistryAvroSerializationSchema.class));
    }

    /**
     * Test whether forSpecific method works
     */
    @Test
    public void testForSpecific_withValidParams_succeeds() {
        assertThat(GlueSchemaRegistryAvroSerializationSchema.forSpecific(User.class, testTopic, configs), notNullValue());
        assertThat(GlueSchemaRegistryAvroSerializationSchema.forSpecific(User.class, testTopic, configs),
                instanceOf(GlueSchemaRegistryAvroSerializationSchema.class));
    }

    /**
     * Test whether serialize method works
     */
    @ParameterizedTest
    @EnumSource(AWSSchemaRegistryConstants.COMPRESSION.class)
    public void testSerialize_withValidParams_succeeds(AWSSchemaRegistryConstants.COMPRESSION compressionType) {
        configs.put(AWSSchemaRegistryConstants.COMPRESSION_TYPE, compressionType.name());

        when(mockSerializationFacade.encode(anyString(), any(com.amazonaws.services.schemaregistry.common.Schema.class), any()))
                .thenReturn(specificBytes);

        GlueSchemaRegistryOutputStreamSerializer glueSchemaRegistryOutputStreamSerializer =
                new GlueSchemaRegistryOutputStreamSerializer(testTopic, configs, mockSerializationFacade);
        GlueSchemaRegistryAvroSchemaCoder glueSchemaRegistryAvroSchemaCoder = new GlueSchemaRegistryAvroSchemaCoder(glueSchemaRegistryOutputStreamSerializer);
        GlueSchemaRegistryAvroSerializationSchema glueSchemaRegistryAvroSerializationSchema =
                new GlueSchemaRegistryAvroSerializationSchema(User.class, null, glueSchemaRegistryAvroSchemaCoder);

        byte[] serializedData = glueSchemaRegistryAvroSerializationSchema.serialize(userDefinedPojo);
        assertThat(serializedData, equalTo(specificBytes));
    }

    /**
     * Test whether serialize method returns null when input object is null
     */
    @Test
    public void testSerialize_withNullObject_returnNull() {
        GlueSchemaRegistryAvroSerializationSchema glueSchemaRegistryAvroSerializationSchema =
                GlueSchemaRegistryAvroSerializationSchema.forSpecific(User.class, testTopic, configs);
        assertThat(glueSchemaRegistryAvroSerializationSchema.serialize(null), nullValue());
    }
}
