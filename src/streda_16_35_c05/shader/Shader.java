package streda_16_35_c05.shader;

@FunctionalInterface
public interface Shader<V, C> {

    C shade(V v);
}
