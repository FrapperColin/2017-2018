<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output method="xml" indent="yes" doctype-system="em.dtd"></xsl:output>    
    
    
    <xsl:template match ="/">
        <em>
            <liste-pays>
                <!-- 3 cas possibles pour "filtrer" les pays -->
                <!-- On va chercher uniquement les pays qui nous interessent, c'est à dire soit le pays est cotoyés par une mer, soit le pays est la source d'un fleuve qui se jette dans une mer, ou encore ce pays est traversé par un fleuve qui se jette dans une mer-->
                <xsl:apply-templates select = "/mondial/country[@car_code = ../sea/id(@country)/@car_code or @car_code = ../river[id(to/@water)/@id = ../sea/@id]/id(source/@country)/@car_code or ../river[id(to/@water)/@id = ../sea/@id]/id(@country)/@car_code = @car_code] " />
            </liste-pays> 
            <!-- Dans un second temps, il nous faut sélectionner les mers -->
            <liste-espace-maritime>
                <xsl:apply-templates select = "/mondial/sea"/>
            </liste-espace-maritime>      
        </em>
    </xsl:template>
    
    <xsl:template match ="country">
        <!-- Pour chaque pays trouvé on l'écrit -->
        <pays id-p = "{@car_code}" nom-p = "{name}" superficie = "{@area}" nbhab = "{population[position() eq last()]/text()}" >
            <!-- On va chercher les fleuves qui nous intérésse pour le pays donné, c'est à dire la source du fleuve est ce pays et ce fleuve 
            se jette dans une mer--> 
            <xsl:apply-templates select="../river[(id(source/@country)/@car_code = current()/@car_code and id(to/@water)/@id = ../sea/@id)] ">    
            </xsl:apply-templates>
        </pays>
    </xsl:template>
    
    <xsl:template match ="river" >
        <!-- Création d'une variable qui sera utilisée pour traiter le cas où l'on a deux pays source d'un fleuve -->
        <xsl:variable name="nbSource" select="count(tokenize(source/@country,' '))"/>
        <!-- Si ce fleuve n'a qu'une seule source -->
        <xsl:if test="$nbSource = 1 ">
            <fleuve id-f = "{@id}" nom-f ="{name}" longueur = "{length}" se-jette = "{to/@water}">
                <!-- Variable tokens pour savoir combien il y a de pays qui parcourt le fleuve -->
                <xsl:variable name="tokens" select="count(tokenize(@country,' '))"/>
                <!-- Deuxième variable dans le cas où l'on a un et un seul pays qui est la source et l'estuaire (on aura donc qu'un seul pays qui 
                le parcourt) afin de déterminer la distance-->
                <xsl:variable name="longueur" select="length"/>
                <!-- Deux cas ; le premier où il y a plusieurs pays parcourut la distance sera donc inconnu,
                    le second ou il n'y a qu'un seul pays et on peut donc déterminer sa distance -->
                <xsl:choose>
                    <xsl:when test="$tokens > 1 ">
                        <xsl:apply-templates select ="id(@country)" mode="first"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- Le fleuve traverse un seul pays -->
                        <xsl:apply-templates select ="id(@country)" mode="second">
                            <!-- On passe en paramètre la variable longueur qui sera égale à la distance-->
                            <xsl:with-param name="longueur" select="$longueur"></xsl:with-param>
                        </xsl:apply-templates>
                    </xsl:otherwise>
                </xsl:choose>
                
                <!-- Une autre version avec foreach, moins procédurale -->
                <!--<xsl:for-each select="tokenize(@country,' ')">   
                    <xsl:choose>
                        <xsl:when test="$tokens > 1 ">
                            <parcourt id-pays = "{.}" distance = "inconnu">     
                            </parcourt>
                        </xsl:when>
                        <xsl:otherwise>
                            <parcourt id-pays = "{.}" distance = "{$longueur}"    >     
                            </parcourt>
                        </xsl:otherwise>
                    </xsl:choose>
                    
                </xsl:for-each>-->
            </fleuve>
        </xsl:if>
        
    </xsl:template>
    
    <!-- Premier cas : le fleuve est parcourut par plusieurs pays, on utilise la modalité d'application des règles pour pouvoir différencier le fait 
    que le fleuve traverse un ou plusieurs pays-->
    <xsl:template match="country" mode="first">
        <parcourt id-pays = "{@car_code}" distance = "inconnu">     
        </parcourt>
    </xsl:template>
    
    <!-- Deuxième cas : le fleuve est parcourut par un seul pays, on utilise la modalité d'application des règles pour pouvoir différencier le fait 
    que le fleuve traverse un ou plusieurs pays-->
    <xsl:template match="country" mode="second">
        <xsl:param name="longueur"></xsl:param>
        <parcourt id-pays = "{@car_code}" distance = "{$longueur}">     
        </parcourt>
    </xsl:template>
    
    <!-- Deuxième "partie"-->
    <xsl:template match ="sea">
        <espace-maritime id-e = "{@id}" nom-e = "{name}" type = "inconnu">
            <!-- On va chercher chaque pays cotoyé par cette mer-->
            <xsl:apply-templates select="id(@country)" mode="third"/>
            
            <!-- Une autre version avec foreach, moins procédurale -->
            <!--<xsl:for-each select="tokenize(@country,' ')">
                <cotoie id-p = "{.}">     
                </cotoie>
            </xsl:for-each>-->
        </espace-maritime>
    </xsl:template>
    
    <!-- Ce sont les pays cotoyés par une mer précise, on utilise la modalité d'application des règles pour pouvoir différencier les match-->
    <xsl:template match="country" mode="third">
        <cotoie id-p = "{@car_code}">     
        </cotoie>
    </xsl:template>
</xsl:stylesheet>