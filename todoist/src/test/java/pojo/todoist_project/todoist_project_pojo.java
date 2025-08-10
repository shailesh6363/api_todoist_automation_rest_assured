package pojo.todoist_project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class todoist_project_pojo {
    private String id;
    private String color;
    private String name;
    private String role;
    private boolean is_shared;
    private boolean public_access;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isIs_shared() { return is_shared; }
    public void setIs_shared(boolean is_shared) { this.is_shared = is_shared; }

    public boolean isPublic_access() { return public_access; }
    public void setPublic_access(boolean public_access) { this.public_access = public_access; }
}