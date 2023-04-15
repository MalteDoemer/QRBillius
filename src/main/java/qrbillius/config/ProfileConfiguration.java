package qrbillius.config;

import net.codecrete.qrbill.generator.Address;
import net.codecrete.qrbill.generator.Language;

public record ProfileConfiguration(String account, Address address, Language language) {
}
