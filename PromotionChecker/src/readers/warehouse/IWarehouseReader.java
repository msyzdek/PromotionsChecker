package readers.warehouse;

import db.entities.Warehouse;

/**
 *
 * @author Miro
 */
public interface IWarehouseReader {
    Warehouse getNext();

    public boolean hasNext();
}
