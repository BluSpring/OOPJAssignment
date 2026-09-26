package xyz.bluspring.systems.hms.role.manager;

import xyz.bluspring.systems.hms.utils.Utils;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public class Department {
    private String id;
    private String departmentName;
    private String description;

    public Department(String id, String departmentName, String description) {
        this.id = id;
        this.departmentName = departmentName;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return id + "," + departmentName + "," + description;
    }

    // Serializer for reading and writing department data
    public static class Serializer extends DataSerializer<Department> {
        public Serializer() {
            super(Department.class);
        }

        @Override
        public String serialize(Department value) {
            return DataSerializers.writeSegmentedLine(Utils.allToStrings(value.getId(), value.getDepartmentName(), value.getDescription()));
        }

        @Override
        public Department deserialize(String data) {
            var split = DataSerializers.readSegmentedLine(data);
            return new Department(split.get(0), split.get(1), split.get(2));
        }
    }

    static {
        DataSerializers.register("department", new Serializer());
    }

    public static void init() {
    }
}
