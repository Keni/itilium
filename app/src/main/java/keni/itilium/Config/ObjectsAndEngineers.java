package keni.itilium.Config;

/**
 * Created by Keni on 06.07.2016.
 */
public class ObjectsAndEngineers
{
    private String id, name;

    public ObjectsAndEngineers(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return name;
    }
}
