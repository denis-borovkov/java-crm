package com.denisborovkov.interfaces;

import java.util.Map;

public interface ClientRepository extends CrudRepository<ClientDetails, Long> {
    Map<Long, ClientDetails> getClientDatabase();
}
