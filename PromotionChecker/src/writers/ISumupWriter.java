/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package writers;

import calculated.Sumup;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Miro
 */
public interface ISumupWriter {
    void writeNext(Sumup sumup);
    void saveToFile(FileOutputStream xls) throws IOException;
}
