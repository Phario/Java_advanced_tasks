module lib {
    requires static lombok;
    requires java.management;

    exports pl.pwr.ite.dynak.beans;
    exports pl.pwr.ite.dynak;
    exports pl.pwr.ite.dynak.utils;
    exports pl.pwr.ite.dynak.services;
}