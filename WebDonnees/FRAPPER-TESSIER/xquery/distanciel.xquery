<em>
    <liste-pays>
    {
        for $country in doc("mondial.xml")/mondial/country[@car_code = ../sea/id(@country)/@car_code or @car_code = ../river[id(to/@water)/@id = ../sea/@id]/id(source/@country)/@car_code or ../river[id(to/@water)/@id = ../sea/@id]/id(@country)/@car_code = @car_code]
        return <pays id-p="{data($country/@car_code)}" nom-p="{data($country/name)}" superficie = "{data($country/@area)}" nbhab = "{data($country/population[position() eq last()]/text())}">
        {
            for $river in doc("mondial.xml")/mondial/river[(id(source/@country)/@car_code = $country/@car_code and id(to/@water)/@id = ../sea/@id)]
            let $nb_river := count(tokenize($river/source/@country, ' '))
            return if ($nb_river = 1) 
            then <fleuve id-f="{data($river/@id)}" nom-f="{data($river/name)}" longueur="{data($river/length)}" se-jette = "{data($river/to/@water)}">
            {
                for $parcourt in tokenize($river/@country, ' ')
                return if ($nb_river > 1) 
                then <parcourt id-pays = "{data($parcourt)}" distance = "inconnu"></parcourt>
                else <parcourt id-pays = "{data($parcourt)}" distance = "{data($river/length)}"></parcourt>
            }
            </fleuve>
            else ()
        }
        </pays>
    }
    </liste-pays>

    <liste-espace-maritime>
    {
        for $sea in doc("mondial.xml")/mondial/sea
        return <espace-maritime id-e="{data($sea/@id)}" nom-e="{data($sea/name)}" type="inconnu" >
            {
                for $cotoie in tokenize($sea/@country,"\s+")
                return <cotoie id-p="{data($cotoie)}"></cotoie>
            }
        </espace-maritime> 
    }
    </liste-espace-maritime>
</em>