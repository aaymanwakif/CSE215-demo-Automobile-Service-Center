package autoservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServiceCenterRepository {
    private static final Path DATA_FILE = Path.of("service-center-data.csv");
    private static final Path LEGACY_DATA_FILE = Path.of("service-center-data.ser");
    private final ServiceCenterTextStore textStore = new ServiceCenterTextStore();

    public ServiceCenter load() throws IOException {
        if (Files.exists(DATA_FILE)) return textStore.load(DATA_FILE);
        if (!Files.exists(LEGACY_DATA_FILE)) return new ServiceCenter();

        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(LEGACY_DATA_FILE))) {
            Object data = input.readObject();
            if (!(data instanceof ServiceCenter)) {
                throw new IOException("The previous data file does not contain service center data.");
            }
            ServiceCenter center = (ServiceCenter) data;
            save(center);
            return center;
        } catch (ClassNotFoundException e) {
            throw new IOException("Could not read the previous data file.", e);
        }
    }

    public void save(ServiceCenter center) throws IOException {
        textStore.save(center, DATA_FILE);
    }
}
