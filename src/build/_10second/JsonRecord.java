package build._10second;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Record;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sets;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.totallylazy.reflection.Fields;
import com.googlecode.totallylazy.reflection.Reflection;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.googlecode.totallylazy.Classes.allClasses;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.Sets.union;
import static com.googlecode.totallylazy.predicates.Predicates.not;
import static com.googlecode.totallylazy.predicates.Predicates.where;
import static com.googlecode.totallylazy.reflection.Fields.fields;
import static com.googlecode.totallylazy.reflection.Fields.modifiers;
import static com.googlecode.totallylazy.reflection.Fields.nonSyntheticFields;
import static com.googlecode.totallylazy.reflection.Reflection.synthetic;
import static java.util.Collections.unmodifiableSet;

public abstract class JsonRecord extends AbstractMap<String, Object> {
    private final Map<String, Object> _otherFields = new HashMap<>();

    public static <T extends JsonRecord> T create(Class<T> recordType, Map<String, Object> data) {
        try {
            T instance = recordType.newInstance();
            for (Entry<String, Object> entry : data.entrySet()) {
                instance.put(entry.getKey(), entry.getValue());
            }
            return instance;
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return unmodifiableSet(union(fieldSet(), _otherFields.entrySet()));
    }

    private Set<Entry<String, Object>> fieldSet() {
        Sequence<Field> fields = fields();
        return set(fields.
                reject(where(modifiers, synthetic)).
                map(f -> pair(f.getName(), Fields.get(f, this))));
    }

    private Sequence<Field> fields() {
        Sequence<Class<?>> classes = allClasses(getClass()).
                reject(Class::isInterface).
                takeWhile(c -> !c.equals(JsonRecord.class));
        return classes.flatMap(Fields.fields());
    }

    @Override
    public Object get(Object key) {
        return field(key.toString()).
                map(Fields.value(this)).
                getOrElse(() -> _otherFields.get(key));
    }

    private Option<Field> field(String name) {
        return fields().
                find(f -> f.getName().equalsIgnoreCase(name)).
                map(Fields::access);
    }

    @Override
    public Object put(String key, Object value) {
        try {
            Option<Field> field = field(key);
            if (field.isDefined()) {
                Field actual = field.get();
                actual.set(this, value);
                return Fields.get(actual, this);
            } else {
                return _otherFields.put(key, value);
            }
        } catch (IllegalAccessException e) {
            throw lazyException(e);
        }
    }

    @Override
    public String toString() {
        return Json.json(this);
    }
}
