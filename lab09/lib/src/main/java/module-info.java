module lib {
    requires jakarta.xml.bind;
    requires lombok;

    opens pl.pwr.ite.dynak.lib.processors to jakarta.xml.bind;


    exports pl.pwr.ite.dynak.lib.processors;
    exports pl.pwr.ite.dynak.lib.utils;
    opens pl.pwr.ite.dynak.lib.utils to jakarta.xml.bind;
}