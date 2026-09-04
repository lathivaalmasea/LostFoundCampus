package Model.Barang;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ModelTableBarang extends AbstractTableModel {
    private List<ModelBarang> listBarang;

    public ModelTableBarang(List<ModelBarang> listBarang) {
        this.listBarang = listBarang;
    }

    // Nama kolom yang lebih jelas dan informatif
    String kolom[] = {
        "ID",
        "Nama Barang",
        "Kategori",
        "Lokasi",
        "Status Barang",      // "Hilang" / "Ditemukan" — lebih jelas dari sebelumnya "Status"
        "Status Klaim",       // "Belum Diklaim" / "Sudah Diklaim" / "Ditarik" — lebih jelas dari "Claim"
        "Klaim Pending",      // jumlah request yg belum ditinjau — lebih jelas dari "Request Claim"
        "Dilaporkan Pada"     // lebih jelas dari "Waktu"
    };

    @Override
    public int getRowCount() {
        return listBarang.size();
    }

    @Override
    public int getColumnCount() {
        return kolom.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ModelBarang b = listBarang.get(rowIndex);
        switch (columnIndex) {
            case 0: return b.getId();
            case 1: return b.getNamaBarang();
            case 2: return b.getKategori();
            case 3: return b.getLokasi();
            case 4: return b.getStatus();
            case 5: return b.getStatusClaim();
            case 6:
                // Tampilkan "—" jika tidak ada pending claim agar lebih rapi
                int cnt = b.getPendingClaimCount();
                return cnt > 0 ? cnt + " pending" : "—";
            case 7: return b.getCreatedAt();
            default: return null;
        }
    }
}
