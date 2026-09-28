package xyz.bluspring.systems.hms.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.data.room.HospitalRoom;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public class AdminDataStorage {
    public static final AdminDataStorage INSTANCE = new AdminDataStorage();

    private static final File INSURANCE_NETWORKS_FILE = DataSerializers.getPath("insurance_networks.txt");
    private static final File HOSPITAL_ROOMS_FILE = DataSerializers.getPath("hospital_rooms.txt");
    private static final File CONFIG_FILE = DataSerializers.getPath("hospital_config.txt");

    private AdminDataStorage() {
    }

    private HospitalConfig config = new HospitalConfig(100f);
    private final List<InsuranceNetwork> insuranceNetworks = new ArrayList<>();
    private final List<HospitalRoom<?>> hospitalRooms = new ArrayList<>();

    public List<InsuranceNetwork> getInsuranceNetworks() {
        return insuranceNetworks;
    }

    public List<HospitalRoom<?>> getHospitalRooms() {
        return hospitalRooms;
    }

    public float getBaseConsultationRate() {
        return this.config.baseConfigurationRate();
    }

    public void setBaseConsultationRate(float baseConsultationRate) {
        this.config = new HospitalConfig(baseConsultationRate);
        this.save();
    }

    public void save() {
        DataSerializers.serializeValue(HospitalConfig.SERIALIZER, CONFIG_FILE, config);
        DataSerializers.serializeValues(InsuranceNetwork.SERIALIZER, INSURANCE_NETWORKS_FILE, insuranceNetworks);
        DataSerializers.serializeValues(HospitalRoom.SERIALIZER.get(), HOSPITAL_ROOMS_FILE, hospitalRooms);
    }

    public void load() {
        var config = DataSerializers.deserializeFile(HospitalConfig.SERIALIZER, CONFIG_FILE);
        if (config != null) {
            this.config = config;
        }

        DataSerializers.deserializeLines(InsuranceNetwork.SERIALIZER, INSURANCE_NETWORKS_FILE, insuranceNetworks);
        DataSerializers.deserializeLines(HospitalRoom.SERIALIZER.get(), HOSPITAL_ROOMS_FILE, hospitalRooms);
    }
}
