package net.exenberger.mystore.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class IPAddressDelegateTest {

    @Test
    void resolveIPAddress() {
        var result = new IPAddressDelegate("https://api.ipify.org").resolveIPAddress();
        assertThat(result).isNotBlank();
    }
}
