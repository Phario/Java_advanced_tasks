<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8"/>

    <xsl:template match="/response">
        <html>
            <body>
                <xsl:apply-templates select="row"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="row">
        <p><b>Manufacturer:</b> <xsl:value-of select="manufacturer"/></p>
        <p><b>Date:</b> <xsl:value-of select="substring-before(report_received_date, 'T')"/></p>
        <p><b>Subject:</b> <xsl:value-of select="subject"/></p>
        <p><b>Component:</b> <xsl:value-of select="component"/></p>
        <p><b>Potentially Affected:</b> <xsl:value-of select="potentially_affected"/></p>
        <hr/>
    </xsl:template>

</xsl:stylesheet>
