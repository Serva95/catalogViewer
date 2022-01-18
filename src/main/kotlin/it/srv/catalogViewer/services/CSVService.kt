package it.srv.catalogViewer.services

import it.srv.catalogViewer.dao.AssetsDAO
import it.srv.catalogViewer.model.Asset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.PrintWriter


@Service
@Transactional
class CSVService {

    @Autowired
    lateinit var assetDAO: AssetsDAO

    fun singleAssetPagetoCSVFileStream(pageN: Int, order: Int, fileName: String): InputStream {
        val assets: Iterable<Asset>? = assetDAO.getAll(pageN, order)
        val file = convertAssetsToCSVFile(assets, fileName)
        val fis = FileInputStream(file)
        file.delete()
        return fis
    }

    fun allAssetsToCSVFileStream(fileName: String): InputStream {
        val assets: Iterable<Asset>? = assetDAO.getAllInDB()
        val file = convertAssetsToCSVFile(assets, fileName)
        val fis = FileInputStream(file)
        file.delete()
        return fis
    }

    fun convertAssetsToCSVFile(data: Iterable<Asset>?, fileName: String): File {
        val csvFile = File(fileName)
        PrintWriter(csvFile).use { pw ->
            pw.println("SrcIPv4,DestIPv4,SrcIPv6,DestIPv6,SrcMAC,DestMAC,SrcPort,DestPort,Proto,FirstSeen,LastSeen")
            data!!.map { t ->
                t.toCommaList()
            }.forEach(pw::println)
        }
        return csvFile
    }

}