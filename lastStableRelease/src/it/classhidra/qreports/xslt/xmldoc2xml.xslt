<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" version="1.0" encoding="ISO-8859-1" omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="report">
<item name="report" type="it.classhidra.qreports.report">
		<item name="filename" type="it.classhidra.qreports.filename">
		<xsl:for-each select="filename">
				<item name="name"><xsl:value-of select="@name"/></item>
		</xsl:for-each>	
		</item>	
		<item name="sql">
			<xsl:value-of select="sql"/>
		</item>
		<items name="parameters">
		<xsl:for-each select="parameters/parameter">
				<item name="parameter" type="it.classhidra.qreports.parameter">
					<item name="name"><xsl:value-of select="@name"/></item>
					<item name="description"><xsl:value-of select="@description"/></item>
					<item name="format_input"><xsl:value-of select="@format_input"/></item>
					<item name="format_output"><xsl:value-of select="@format_output"/></item>
					<item name="value_type"><xsl:value-of select="@value_type"/></item>
					<item name="view_type"><xsl:value-of select="@view_type"/></item>
					<item name="exec_type"><xsl:value-of select="@exec_type"/></item>
					<item name="default_value"><xsl:value-of select="@default_value"/></item>
					<item name="length"><xsl:value-of select="@length"/></item>
					<item name="mandatory"><xsl:value-of select="@mandatory"/></item>
					<item name="adaptsql"><xsl:value-of select="@adaptsql"/></item>
				<xsl:for-each select="languagetranslationtable">
					<item name="languagetranslationtable" type="it.classhidra.qreports.languagetranslationtable">
							<item name="id"><xsl:value-of select="@id"/></item>
							<item name="section"><xsl:value-of select="@section"/></item>
					</item>
				</xsl:for-each>
				<xsl:for-each select="source">
			 			<item name="source" type="it.classhidra.qreports.source">
			 				<xsl:for-each select="sql">
			 					<item name="sql">
									<xsl:value-of select="."/>
								</item>
			 				</xsl:for-each>
			 				<xsl:for-each select="list">
			 					<items name="list">
				 						<xsl:for-each select="option">
												<item name="option" type="it.classhidra.qreports.source_item">
														<item name="value"><xsl:value-of select="@value"/></item>
                            <item name="description"><xsl:value-of select="@description"/></item>
												<xsl:for-each select="languagetranslationtable">
													<item name="languagetranslationtable" type="it.classhidra.qreports.languagetranslationtable">
															<item name="id"><xsl:value-of select="@id"/></item>
															<item name="section"><xsl:value-of select="@section"/></item>
													</item>
												</xsl:for-each>
												</item>
				 						</xsl:for-each>
								</items>
			 				</xsl:for-each>
					</item>
				</xsl:for-each>		
				</item>
		</xsl:for-each>
		</items>
</item>
</xsl:template>

</xsl:stylesheet>