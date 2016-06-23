package Primitives;

public class OccurenceHolder<T>
{
    public T area;
    public long occurences;

    public OccurenceHolder(T area, long occurences)
    {
        this.area = area;
        this.occurences = occurences;
    }
}
