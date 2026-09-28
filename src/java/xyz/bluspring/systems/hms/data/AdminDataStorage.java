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

    private AdminDataStorage() {
    }

    private final List<InsuranceNetwork> insuranceNetworks = new ArrayList<>();
    private final List<HospitalRoom<?>> hospitalRooms = new ArrayList<>();

    public List<InsuranceNetwork> getInsuranceNetworks() {
        return insuranceNetworks;
    }

    public List<HospitalRoom<?>> getHospitalRooms() {
        return hospitalRooms;
    }

    public void save() {
        DataSerializers.serializeValues(InsuranceNetwork.SERIALIZER, INSURANCE_NETWORKS_FILE, insuranceNetworks);
        DataSerializers.serializeValues(HospitalRoom.SERIALIZER.get(), HOSPITAL_ROOMS_FILE, hospitalRooms);
    }

    public void load() {
        DataSerializers.deserializeLines(InsuranceNetwork.SERIALIZER, INSURANCE_NETWORKS_FILE, insuranceNetworks);
        DataSerializers.deserializeLines(HospitalRoom.SERIALIZER.get(), HOSPITAL_ROOMS_FILE, hospitalRooms);
    }
}
