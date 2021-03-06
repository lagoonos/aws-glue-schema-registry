/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.amazonaws.services.schemaregistry.integration_tests.kafka;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Person extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2004297153405301495L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"com.amazon.aws.kinesis.schemaregistry.kafka.serdes\",\"fields\":[{\"name\":\"firstName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"First Name\"},{\"name\":\"lastName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Last Name\"},{\"name\":\"age\",\"type\":\"int\",\"doc\":\"Age\"},{\"name\":\"height\",\"type\":\"float\",\"doc\":\"Height in centimeters\"},{\"name\":\"employed\",\"type\":\"boolean\",\"doc\":\"Employment status\",\"default\":true}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Person> ENCODER =
      new BinaryMessageEncoder<Person>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Person> DECODER =
      new BinaryMessageDecoder<Person>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<Person> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<Person> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Person>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this Person to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a Person from a ByteBuffer. */
  public static Person fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** First Name */
   private String firstName;
  /** Last Name */
   private String lastName;
  /** Age */
   private int age;
  /** Height in centimeters */
   private float height;
  /** Employment status */
   private boolean employed;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Person() {}

  /**
   * All-args constructor.
   * @param firstName First Name
   * @param lastName Last Name
   * @param age Age
   * @param height Height in centimeters
   * @param employed Employment status
   */
  public Person(String firstName, String lastName, Integer age, Float height, Boolean employed) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.height = height;
    this.employed = employed;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public Object get(int field$) {
    switch (field$) {
    case 0: return firstName;
    case 1: return lastName;
    case 2: return age;
    case 3: return height;
    case 4: return employed;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: firstName = (String)value$; break;
    case 1: lastName = (String)value$; break;
    case 2: age = (Integer)value$; break;
    case 3: height = (Float)value$; break;
    case 4: employed = (Boolean)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'firstName' field.
   * @return First Name
   */
  public String getFirstName() {
    return firstName;
  }


  /**
   * Gets the value of the 'lastName' field.
   * @return Last Name
   */
  public String getLastName() {
    return lastName;
  }


  /**
   * Gets the value of the 'age' field.
   * @return Age
   */
  public Integer getAge() {
    return age;
  }


  /**
   * Gets the value of the 'height' field.
   * @return Height in centimeters
   */
  public Float getHeight() {
    return height;
  }


  /**
   * Gets the value of the 'employed' field.
   * @return Employment status
   */
  public Boolean getEmployed() {
    return employed;
  }


  /**
   * Creates a new Person RecordBuilder.
   * @return A new Person RecordBuilder
   */
  public static Person.Builder newBuilder() {
    return new Person.Builder();
  }

  /**
   * Creates a new Person RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Person RecordBuilder
   */
  public static Person.Builder newBuilder(Person.Builder other) {
    return new Person.Builder(other);
  }

  /**
   * Creates a new Person RecordBuilder by copying an existing Person instance.
   * @param other The existing instance to copy.
   * @return A new Person RecordBuilder
   */
  public static Person.Builder newBuilder(Person other) {
    return new Person.Builder(other);
  }

  /**
   * RecordBuilder for Person instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Person>
    implements org.apache.avro.data.RecordBuilder<Person> {

    /** First Name */
    private String firstName;
    /** Last Name */
    private String lastName;
    /** Age */
    private int age;
    /** Height in centimeters */
    private float height;
    /** Employment status */
    private boolean employed;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(Person.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.firstName)) {
        this.firstName = data().deepCopy(fields()[0].schema(), other.firstName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.lastName)) {
        this.lastName = data().deepCopy(fields()[1].schema(), other.lastName);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.age)) {
        this.age = data().deepCopy(fields()[2].schema(), other.age);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.height)) {
        this.height = data().deepCopy(fields()[3].schema(), other.height);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.employed)) {
        this.employed = data().deepCopy(fields()[4].schema(), other.employed);
        fieldSetFlags()[4] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing Person instance
     * @param other The existing instance to copy.
     */
    private Builder(Person other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.firstName)) {
        this.firstName = data().deepCopy(fields()[0].schema(), other.firstName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.lastName)) {
        this.lastName = data().deepCopy(fields()[1].schema(), other.lastName);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.age)) {
        this.age = data().deepCopy(fields()[2].schema(), other.age);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.height)) {
        this.height = data().deepCopy(fields()[3].schema(), other.height);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.employed)) {
        this.employed = data().deepCopy(fields()[4].schema(), other.employed);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'firstName' field.
      * First Name
      * @return The value.
      */
    public String getFirstName() {
      return firstName;
    }

    /**
      * Sets the value of the 'firstName' field.
      * First Name
      * @param value The value of 'firstName'.
      * @return This builder.
      */
    public Person.Builder setFirstName(String value) {
      validate(fields()[0], value);
      this.firstName = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'firstName' field has been set.
      * First Name
      * @return True if the 'firstName' field has been set, false otherwise.
      */
    public boolean hasFirstName() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'firstName' field.
      * First Name
      * @return This builder.
      */
    public Person.Builder clearFirstName() {
      firstName = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'lastName' field.
      * Last Name
      * @return The value.
      */
    public String getLastName() {
      return lastName;
    }

    /**
      * Sets the value of the 'lastName' field.
      * Last Name
      * @param value The value of 'lastName'.
      * @return This builder.
      */
    public Person.Builder setLastName(String value) {
      validate(fields()[1], value);
      this.lastName = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'lastName' field has been set.
      * Last Name
      * @return True if the 'lastName' field has been set, false otherwise.
      */
    public boolean hasLastName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'lastName' field.
      * Last Name
      * @return This builder.
      */
    public Person.Builder clearLastName() {
      lastName = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'age' field.
      * Age
      * @return The value.
      */
    public Integer getAge() {
      return age;
    }

    /**
      * Sets the value of the 'age' field.
      * Age
      * @param value The value of 'age'.
      * @return This builder.
      */
    public Person.Builder setAge(int value) {
      validate(fields()[2], value);
      this.age = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'age' field has been set.
      * Age
      * @return True if the 'age' field has been set, false otherwise.
      */
    public boolean hasAge() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'age' field.
      * Age
      * @return This builder.
      */
    public Person.Builder clearAge() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'height' field.
      * Height in centimeters
      * @return The value.
      */
    public Float getHeight() {
      return height;
    }

    /**
      * Sets the value of the 'height' field.
      * Height in centimeters
      * @param value The value of 'height'.
      * @return This builder.
      */
    public Person.Builder setHeight(float value) {
      validate(fields()[3], value);
      this.height = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'height' field has been set.
      * Height in centimeters
      * @return True if the 'height' field has been set, false otherwise.
      */
    public boolean hasHeight() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'height' field.
      * Height in centimeters
      * @return This builder.
      */
    public Person.Builder clearHeight() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'employed' field.
      * Employment status
      * @return The value.
      */
    public Boolean getEmployed() {
      return employed;
    }

    /**
      * Sets the value of the 'employed' field.
      * Employment status
      * @param value The value of 'employed'.
      * @return This builder.
      */
    public Person.Builder setEmployed(boolean value) {
      validate(fields()[4], value);
      this.employed = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'employed' field has been set.
      * Employment status
      * @return True if the 'employed' field has been set, false otherwise.
      */
    public boolean hasEmployed() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'employed' field.
      * Employment status
      * @return This builder.
      */
    public Person.Builder clearEmployed() {
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Person build() {
      try {
        Person record = new Person();
        record.firstName = fieldSetFlags()[0] ? this.firstName : (String) defaultValue(fields()[0]);
        record.lastName = fieldSetFlags()[1] ? this.lastName : (String) defaultValue(fields()[1]);
        record.age = fieldSetFlags()[2] ? this.age : (Integer) defaultValue(fields()[2]);
        record.height = fieldSetFlags()[3] ? this.height : (Float) defaultValue(fields()[3]);
        record.employed = fieldSetFlags()[4] ? this.employed : (Boolean) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Person>
    WRITER$ = (org.apache.avro.io.DatumWriter<Person>)MODEL$.createDatumWriter(SCHEMA$);

  @Override
  public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Person>
    READER$ = (org.apache.avro.io.DatumReader<Person>)MODEL$.createDatumReader(SCHEMA$);

  @Override
  public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
