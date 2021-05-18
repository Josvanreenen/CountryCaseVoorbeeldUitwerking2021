package nl.hu.bep.countrycase.persistence;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import nl.hu.bep.countrycase.model.World;

import java.io.*;

public class PersistenceManager {
    private static final String ENDPOINT = "https://bepcountrycase.blob.core.windows.net/";
    private static final String SASTOKEN = "?sv=2019-10-10&ss=bfqt&srt=co&sp=rwdlacupx&se=2020-05-06T08:40:17Z&st=2020-05-06T00:40:17Z&spr=https&sig=yQ8hbIw57hwyeWiCb4jpnlWiKhAn9VFyc8FzKz9dtFE%3D";
    private static final String CONTAINER = "worldcontainer";

    private static BlobContainerClient blobContainer = new BlobContainerClientBuilder()
                                                            .endpoint(ENDPOINT)
                                                            .sasToken(SASTOKEN)
                                                            .containerName(CONTAINER)
                                                            .buildClient();

    private PersistenceManager() {
    }

    public static void loadWorldFromAzure() throws IOException, ClassNotFoundException {
        if (blobContainer.exists()) {
            BlobClient blob = blobContainer.getBlobClient("world_blob");

            if (blob.exists()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                blob.download(baos);

                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);

                World loadedWorld = (World)ois.readObject();
                World.setWorld(loadedWorld);

                baos.close();
                ois.close();
            } else throw new IllegalStateException("container not found, loading default data");
        }
    }

    public static void saveWorldToAzure() throws IOException {
        if (!blobContainer.exists()) {
            blobContainer.create();
        }

        BlobClient blob = blobContainer.getBlobClient("world_blob");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(World.getWorld());

        byte[] bytez = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytez);
        blob.upload(bais, bytez.length, true);

        oos.close();
        bais.close();
    }
}