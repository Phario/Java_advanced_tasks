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
                        background: #0a0f0a;
                        color: #d4e8d4;
                        padding: 2.5rem;
                    }

                    header {
                        display: flex;
                        align-items: center;
                        gap: 1.2rem;
                        margin-bottom: 2.5rem;
                        border-bottom: 2px solid #00a550;
                        padding-bottom: 1.2rem;
                    }

                    .logo-bar {
                        width: 6px;
                        height: 2.2rem;
                        background: #00a550;
                        border-radius: 3px;
                    }

                    h1 {
                        font-size: 1.5rem;
                        font-weight: 700;
                        color: #ffffff;
                        letter-spacing: 0.04em;
                        text-transform: uppercase;
                    }

                    h1 span {
                        color: #00a550;
                    }

                    .subtitle {
                        font-size: 0.78rem;
                        color: #5a8a5a;
                        letter-spacing: 0.08em;
                        text-transform: uppercase;
                        margin-top: 0.2rem;
                    }

                    .grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
                        gap: 1.1rem;
                    }

                    .card {
                        background: #0d1a0d;
                        border: 1px solid #1a3a1a;
                        border-left: 3px solid #00a550;
                        border-radius: 6px;
                        padding: 1.2rem 1.4rem;
                        display: flex;
                        flex-direction: column;
                        gap: 0.8rem;
                        transition: border-color 0.2s;
                    }

                    .card:hover {
                        border-left-color: #00cc66;
                        background: #0f1f0f;
                    }

                    .card-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: flex-start;
                        gap: 0.5rem;
                    }

                    .manufacturer {
                        font-size: 0.95rem;
                        font-weight: 700;
                        color: #00a550;
                        letter-spacing: 0.02em;
                        text-transform: uppercase;
                    }

                    .date {
                        font-size: 0.72rem;
                        color: #4a7a4a;
                        white-space: nowrap;
                        padding-top: 0.2rem;
                        font-family: monospace;
                        letter-spacing: 0.05em;
                    }

                    .subject {
                        font-size: 0.88rem;
                        color: #c0dcc0;
                        line-height: 1.5;
                    }

                    .meta {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        border-top: 1px solid #1a3a1a;
                        padding-top: 0.7rem;
                        gap: 0.5rem;
                    }

                    .component-tag {
                        background: #0a1f0a;
                        color: #00a550;
                        border: 1px solid #1a4a1a;
                        font-size: 0.68rem;
                        font-weight: 600;
                        letter-spacing: 0.08em;
                        text-transform: uppercase;
                        padding: 0.2rem 0.65rem;
                        border-radius: 3px;
                    }

                    .affected {
                        font-size: 0.78rem;
                        font-weight: 700;
                        color: #00cc66;
                    }

                    .affected span {
                        color: #4a7a4a;
                        font-weight: 400;
                    }
                </style>
            </head>
            <body>
                <header>
                    <div class="logo-bar"/>
                    <div>
                        <h1>NHTSA <span>Recalls</span></h1>
                        <div class="subtitle">Vehicle Safety Database</div>
                    </div>
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