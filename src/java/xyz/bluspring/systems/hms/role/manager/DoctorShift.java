package xyz.bluspring.systems.hms.role.manager;

import xyz.bluspring.systems.hms.utils.Utils;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public class DoctorShift {
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

    // Serializer for reading and writing doctor shift data
    public static class Serializer extends DataSerializer<DoctorShift> {
        public Serializer() {
            super(DoctorShift.class);
        }

        @Override
        public String serialize(DoctorShift value) {
            return DataSerializers.writeSegmentedLine(Utils.allToStrings(value.getId(), value.getDoctorName(), value.getDepartmentName(), value.getShiftDate(), value.getShiftType()));
        }

        @Override
        public DoctorShift deserialize(String data) {
            var split = DataSerializers.readSegmentedLine(data);
            return new DoctorShift(split.get(0), split.get(1), split.get(2), split.get(3), split.get(4));
        }
    }

    static {
        DataSerializers.register("doctor_shift", new Serializer());
    }

    public static void init() {
    }
}
