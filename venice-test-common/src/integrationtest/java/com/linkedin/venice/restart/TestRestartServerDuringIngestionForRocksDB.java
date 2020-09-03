package com.linkedin.venice.restart;

import com.linkedin.venice.meta.PersistenceType;
import java.util.Properties;

import static com.linkedin.venice.ConfigKeys.*;
import static com.linkedin.venice.store.rocksdb.RocksDBServerConfig.*;


public class TestRestartServerDuringIngestionForRocksDB extends TestRestartServerDuringIngestion {
  @Override
  protected PersistenceType getPersistenceType() {
    return PersistenceType.ROCKS_DB;
  }

  @Override
  protected Properties getExtraProperties() {
    Properties extraProperties = new Properties();
    return extraProperties;
  }

}
