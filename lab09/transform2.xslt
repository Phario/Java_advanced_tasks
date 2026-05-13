<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/response">
        <html>
            <head>
                <title>NHTSA Recalls</title>
                <style>
                    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

                    body {
                        font-family: 'Segoe UI', Arial, sans-serif;
                        background: #0f172a;
                        color: #e2e8f0;
                        padding: 2rem;
                    }

                    header {
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                        margin-bottom: 2rem;
                    }

                    header .badge {
                        background: #ef4444;
                        color: #fff;
                        font-size: 0.7rem;
                        font-weight: 700;
                        letter-spacing: 0.1em;
                        text-transform: uppercase;
                        padding: 0.3rem 0.7rem;
                        border-radius: 4px;
                    }

                    h1 {
                        font-size: 1.6rem;
                        font-weight: 700;
                        color: #f8fafc;
                    }

                    .grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
                        gap: 1.25rem;
                    }

                    .card {
                        background: #1e293b;
                        border: 1px solid #334155;
                        border-radius: 10px;
                        padding: 1.25rem 1.4rem;
                        display: flex;
                        flex-direction: column;
                        gap: 0.75rem;
                    }

                    .card-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: flex-start;
                        gap: 0.5rem;
                    }

                    .manufacturer {
                        font-size: 1rem;
                        font-weight: 600;
                        color: #38bdf8;
                        line-height: 1.3;
                    }

                    .date {
                        font-size: 0.75rem;
                        color: #94a3b8;
                        white-space: nowrap;
                        padding-top: 0.15rem;
                    }

                    .subject {
                        font-size: 0.9rem;
                        color: #f1f5f9;
                        line-height: 1.45;
                    }

                    .meta {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        gap: 0.5rem;
                        border-top: 1px solid #334155;
                        padding-top: 0.65rem;
                    }

                    .component-tag {
                        background: #0f2744;
                        color: #7dd3fc;
                        font-size: 0.7rem;
                        font-weight: 600;
                        letter-spacing: 0.05em;
                        text-transform: uppercase;
                        padding: 0.25rem 0.6rem;
                        border-radius: 20px;
                        border: 1px solid #1e4a7a;
                    }

                    .affected {
                        font-size: 0.78rem;
                        color: #fbbf24;
                        font-weight: 600;
                    }

                    .affected span {
                        color: #94a3b8;
                        font-weight: 400;
                    }
                </style>
            </head>
            <body>
                <header>
                    <div class="badge">Safety Alert</div>
                    <h1>NHTSA Vehicle Recalls</h1>
                </header>
                <div class="grid">
                    <xsl:apply-templates select="row"/>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="row">
        <div class="card">
            <div class="card-header">
                <div class="manufacturer"><xsl:value-of select="manufacturer"/></div>
                <div class="date"><xsl:value-of select="substring-before(report_received_date, 'T')"/></div>
            </div>
            <div class="subject"><xsl:value-of select="normalize-space(subject)"/></div>
            <div class="meta">
                <span class="component-tag"><xsl:value-of select="component"/></span>
                <span class="affected">
                    <xsl:value-of select="format-number(potentially_affected, '#,##0')"/>
                    <span> affected</span>
                </span>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>
