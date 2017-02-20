<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

    <xsl:output method="text" encoding="UTF8" media-type="text/plain" />

    <xsl:template match="/ | @* | node()">
        <xsl:for-each select="//test-suite[@type='TestFixture']">
            <xsl:text></xsl:text>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>