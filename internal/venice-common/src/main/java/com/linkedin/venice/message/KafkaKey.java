package com.linkedin.venice.message;

import com.linkedin.venice.kafka.protocol.enums.MessageType;
import com.linkedin.venice.storage.protocol.ChunkId;
import com.linkedin.venice.utils.ByteUtils;
import javax.annotation.Nonnull;


/**
 * Class which stores the components of a Kafka Key, and is the format specified in the
 * {@link com.linkedin.venice.serialization.KafkaKeySerializer}.
 */
public class KafkaKey {
  private final byte keyHeaderByte;
  private final byte[] key; // TODO: Consider whether we may want to use a ByteBuffer here
  private final ChunkId chunkId;

  public KafkaKey(ChunkId chunkId, byte[] key) {
    this(MessageType.PUT.getKeyHeaderByte(), key, chunkId);
  }

  public KafkaKey(@Nonnull MessageType messageType, byte[] key) {
    this(messageType.getKeyHeaderByte(), key);
  }

  public KafkaKey(byte keyHeaderByte, byte[] key) {
    this(keyHeaderByte, key, null);
  }

  public KafkaKey(byte keyHeaderByte, byte[] key, ChunkId chunkId) {
    this.keyHeaderByte = keyHeaderByte;
    this.key = key;
    this.chunkId = chunkId;
  }

  /**
   * The key header byte is the first byte in the content of the Kafka key. This is
   * significant because it affects Kafka's Log Compaction. For {@link MessageType#PUT}
   * and {@link MessageType#DELETE}, we want to use the exact same Kafka key, so the
   * header byte needs to be the same. For control messages, however, we want them
   * to be name-spaced on their own, so that they do not collide with the data when
   * Log Compaction runs.
   *
   * @return a single byte: '0' for PUT and DELETE, or '2' for CONTROL_MESSAGE
   */
  public byte getKeyHeaderByte() {
    return keyHeaderByte;
  }

  /**
   * @return true if this key corresponds to a control message, and false otherwise.
   */
  public boolean isControlMessage() {
    return keyHeaderByte == MessageType.CONTROL_MESSAGE.getKeyHeaderByte();
  }

  public boolean hasChunkId() {
    return chunkId != null;
  }

  /**
   * @return the content of the key (everything beyond the first byte)
   */
  public byte[] getKey() {
    return key;
  }

  public ChunkId getChunkId() {
    return chunkId;
  }

  public int getKeyLength() {
    return key == null ? 0 : key.length;
  }

  public String toString() {
    return getClass().getSimpleName() + "(" + (isControlMessage() ? "CONTROL_MESSAGE" : "PUT or DELETE") + ", "
        + ByteUtils.toHexString(key) + ")";
  }
}
