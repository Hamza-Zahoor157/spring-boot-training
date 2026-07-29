package com.redmath.lecture02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Locale;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;

class Lecture02ApplicationTest {

  @Test
  void init_setsDefaultTimeZoneToUTC() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    assertEquals(TimeZone.getTimeZone("UTC"), TimeZone.getDefault());
  }

  @Test
  void init_setsDefaultLocaleToUS() {
    Locale.setDefault(Locale.US);
    assertEquals(Locale.US, Locale.getDefault());
  }

  @Test
  void main_startsApplicationWithoutError() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    Locale.setDefault(Locale.US);
    assertNotNull(Lecture02Application.class);
  }
}