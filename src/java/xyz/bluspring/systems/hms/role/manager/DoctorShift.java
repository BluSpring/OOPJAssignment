package xyz.bluspring.systems.hms.role.manager;

import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class DoctorShift implements DataSerializable<DoctorShift> {
    public static final DataSerializer<DoctorShift> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.STRING, DoctorShift::getId,
        DataSerializer.STRING, DoctorShift::getDoctorName,
        DataSerializer.STRING, DoctorShift::getDepartmentName,
        DataSerializer.STRING, DoctorShift::getShiftDate,
        DataSerializer.STRING, DoctorShift::getShiftType,
        DoctorShift::new
    );

    private String id;
    private String doctorName;
    private String departmentName;
    private String shiftDate;
    private String shiftType;

    public DoctorShift(String id, String doctorName, String departmentName, String shiftDate, String shiftType) {
        this.id = id;
        this.doctorName = doctorName;
        this.departmentName = departmentName;
        this.shiftDate = shiftDate;
        this.shiftType = shiftType;
    }

    @Override
    public DataSerializer<DoctorShift> getSerializer() {
        return SERIALIZER;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    @Override
    public String toString() {
        return id + "," + doctorName + "," + departmentName + "," + shiftDate + "," + shiftType;
    }
}
