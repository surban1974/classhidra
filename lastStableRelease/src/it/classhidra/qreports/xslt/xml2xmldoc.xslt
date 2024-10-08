<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" version="1.0" encoding="ISO-8859-1" omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="/">
<report>
 	<xsl:for-each select="item/item">
 	
		<xsl:if test="@name='filename' or @name='Filename'">
			<filename>
				<xsl:attribute name="name">
					<xsl:value-of select="."/>
				</xsl:attribute>
			</filename>
		</xsl:if>	

 		<xsl:if test="@name='sql' or @name='Sql'">
			<sql>
				<xsl:value-of select="."/>
			</sql>
		</xsl:if>
	</xsl:for-each>
	<xsl:for-each select="item/items">
		<xsl:if test="@name='parameters' or @name='Parameters'">
			<parameters>
			<xsl:for-each select="item">
				<parameter>
					<xsl:for-each select="item">
		
						<xsl:if test="@name='name' or @name='Name'">
						<xsl:attribute name="name">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
		
						<xsl:if test="@name='description' or @name='Description'">
						<xsl:attribute name="description">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
		
						<xsl:if test="@name='format_input' or @name='Format_input'">
						<xsl:if test=".!=''">
						<xsl:attribute name="format_input">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
						</xsl:if>
		
						<xsl:if test="@name='format_output' or @name='Format_output'">
						<xsl:if test=".!=''">
						<xsl:attribute name="format_output">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
						</xsl:if>
		
						<xsl:if test="@name='value_type' or @name='Value_type'">
						<xsl:attribute name="value_type">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
		
						<xsl:if test="@name='view_type' or @name='View_type'">
						<xsl:attribute name="view_type">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
		
						<xsl:if test="@name='exec_type' or @name='Exec_type'">
						<xsl:attribute name="exec_type">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
		
						<xsl:if test="@name='default_value' or @name='Default_value'">
						<xsl:attribute name="default_value">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
		
						<xsl:if test="@name='length' or @name='Length'">
						<xsl:if test=".!=''">
						<xsl:attribute name="length">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
						</xsl:if>
		
						<xsl:if test="@name='mandatory' or @name='Mandatory'">
						<xsl:if test=".!=''">
						<xsl:attribute name="mandatory">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
						</xsl:if>
						
						<xsl:if test="@name='adaptsql' or @name='Adaptsql'">
						<xsl:if test=".!=''">
						<xsl:attribute name="adaptsql">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
						</xsl:if>
		
					</xsl:for-each>
				<languagetranslationtable>
					<xsl:for-each select="item/item">
		
						<xsl:if test="@name='id' or @name='Id'">
						<xsl:attribute name="id">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
		
						<xsl:if test="@name='section' or @name='Section'">
						<xsl:attribute name="section">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
					</xsl:for-each>
				</languagetranslationtable>
				<source>
					<xsl:if test="item/items/@name='list' or item/items/@name='List'">
					<list>
						<xsl:for-each select="item/items/item">
						<option>
							<xsl:for-each select="item">
							<xsl:if test="@name='value' or @name='Value'">
								<xsl:attribute name="value">
									<xsl:value-of select="."/>
								</xsl:attribute>
							</xsl:if>
		
							<xsl:if test="@name='description' or @name='Description'">
								<xsl:attribute name="description">
									<xsl:value-of select="."/>
								</xsl:attribute>
							</xsl:if>
							</xsl:for-each>
		
							
							<languagetranslationtable>
								<xsl:for-each select="item/item">
									<xsl:if test="@name='id' or @name='Id'">
									<xsl:attribute name="id">
										<xsl:value-of select="."/>
									</xsl:attribute>
									</xsl:if>
		
									<xsl:if test="@name='section' or @name='Section'">
									<xsl:attribute name="section">
										<xsl:value-of select="."/>
									</xsl:attribute>
									</xsl:if>
								</xsl:for-each>
							</languagetranslationtable>
		
						</option>
						</xsl:for-each>
					</list>
					</xsl:if>
					
					<xsl:if test="item/item/@name='sql' or item/item/@name='Sql'">
					<sql>
						<xsl:for-each select="item/item">
							<xsl:if test="@name='sql' or @name='Sql'">
								<xsl:value-of select="."/>
							</xsl:if>
						</xsl:for-each>
					</sql>
					</xsl:if>
				</source>
				</parameter>
			</xsl:for-each>
			</parameters>
		</xsl:if>
		<xsl:if test="@name='transports' or @name='Transports'">
			<transports>
			<xsl:for-each select="item">
				<transport>
					<xsl:for-each select="item">
					
						<xsl:if test="@name='name' or @name='Name'">
						<xsl:attribute name="name">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
						
						<xsl:if test="@name='type' or @name='Type'">
						<xsl:attribute name="type">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>						
		
						<xsl:if test="@name='worker' or @name='Worker'">
						<xsl:attribute name="worker">
							<xsl:value-of select="."/>
						</xsl:attribute>
						</xsl:if>
					
					</xsl:for-each>
					<xsl:for-each select="items">
						<xsl:if test="@name='properties' or @name='Properties'">
							<properties>
								<xsl:for-each select="item">
									<property>
										<xsl:for-each select="item">
					
											<xsl:if test="@name='name' or @name='Name'">
											<xsl:attribute name="name">
												<xsl:value-of select="."/>
											</xsl:attribute>
											</xsl:if>
											<xsl:if test="@name='value' or @name='Value'">
											<xsl:attribute name="value">
												<xsl:value-of select="."/>
											</xsl:attribute>
											</xsl:if>											
										</xsl:for-each>
									</property>	
								</xsl:for-each>
							</properties>
						</xsl:if>
					</xsl:for-each>
				</transport>
			</xsl:for-each>
			</transports>	
		</xsl:if>
	</xsl:for-each>
</report>	
</xsl:template>

</xsl:stylesheet>