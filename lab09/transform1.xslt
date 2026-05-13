<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/response">
        <html>
            <head>
                <title>NHTSA Recalls</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 2rem; background: #f5f5f5; }
                    h1 { color: #333; }
                    table { border-collapse: collapse; width: 100%; background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,0.1); }
                    th { background: #1a3a5c; color: #fff; padding: 10px 14px; text-align: left; }
                    td { padding: 9px 14px; border-bottom: 1px solid #e0e0e0; vertical-align: top; }
                    tr:last-child td { border-bottom: none; }
                    tr:nth-child(even) { background: #f9f9f9; }
                    .affected { text-align: right; }
                    .date { white-space: nowrap; }
                </style>
            </head>
            <body>
                <h1>NHTSA Vehicle Recalls</h1>
                <table>
                    <thead>
                        <tr>
                            <th>Manufacturer</th>
                            <th>Date Received</th>
                            <th>Subject</th>
                            <th>Component</th>
                            <th>Potentially Affected</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="row"/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="row">
        <tr>
            <td><xsl:value-of select="manufacturer"/></td>
            <td class="date">
                <!-- Format 2026-05-07T00:00:00 → 2026-05-07 -->
                <xsl:value-of select="substring-before(report_received_date, 'T')"/>
            </td>
            <td><xsl:value-of select="normalize-space(subject)"/></td>
            <td><xsl:value-of select="component"/></td>
            <td class="affected"><xsl:value-of select="potentially_affected"/></td>
        </tr>
    </xsl:template>

</xsl:stylesheet>