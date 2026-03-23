package pl.pwr.ite.dynak.lib.cache;

public record WeakCacheResult<T>(
        boolean fromCache,
        T fileData
) {}
