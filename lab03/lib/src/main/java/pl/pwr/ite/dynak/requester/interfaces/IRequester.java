package pl.pwr.ite.dynak.requester.interfaces;

import pl.pwr.ite.dynak.requester.models.CharacterData;
import pl.pwr.ite.dynak.requester.models.PlanetData;
import pl.pwr.ite.dynak.requester.models.StarshipData;

public interface IRequester {
    StarshipData getRandomStarshipData();
    PlanetData getRandomPlanetData();
    CharacterData getRandomCharacterData();
}
