package xyz.bluspring.systems.hms.utils.data;

public interface DataSerializable<T extends DataSerializable<T>> {
    DataSerializer<T> getSerializer();
}
