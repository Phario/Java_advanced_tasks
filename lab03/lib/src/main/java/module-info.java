module lib {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires static lombok;

    exports pl.pwr.ite.dynak.requester.util;
    exports pl.pwr.ite.dynak.requester.models;
    exports pl.pwr.ite.dynak.requester.interfaces;
    exports pl.pwr.ite.dynak.requester.enums;
}