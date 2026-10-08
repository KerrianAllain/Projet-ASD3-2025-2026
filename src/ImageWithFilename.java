public class ImageWithFilename {
    private final ImagePNG image;
    private final String filename;

    public ImageWithFilename(ImagePNG image, String filename) {
        this.filename = filename;
        this.image = image;
    }

    public ImagePNG image() {
        return image;
    }

    public String filename() {
        return filename;
    }
}
