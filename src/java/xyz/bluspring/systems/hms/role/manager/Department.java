package xyz.bluspring.systems.hms.role.manager;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class Department {
    public static final DataSerializer<Department> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.STRING, Department::getId,
        DataSerializer.STRING, Department::getDepartmentName,
        DataSerializer.STRING, Department::getDescription,
        Department::new
    );

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
}
