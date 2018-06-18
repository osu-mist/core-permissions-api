package edu.oregonstate.mist.permissionsapi.db

interface PermissionsDAO extends Closeable {

    @Override
    void close()
}
