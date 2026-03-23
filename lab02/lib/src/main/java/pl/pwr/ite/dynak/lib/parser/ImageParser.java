package pl.pwr.ite.dynak.lib.parser;

import javafx.scene.image.Image;
import pl.pwr.ite.dynak.lib.models.ImageData;

import java.io.File;
import java.io.IOException;

public class ImageParser implements IFileParser<ImageData> {

    @Override
    public ImageData parse(File file) throws IOException {
        Image image = new Image(file.toURI().toString());
        return new ImageData(image, image.getHeight(), image.getWidth());
    }
}
