package it.srv.catalogViewer

import it.srv.catalogViewer.model.Asset
import it.srv.catalogViewer.services.CSVService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView


@RestController
@RequestMapping("/export")
class ExportController {

    @Autowired
    lateinit var csvService: CSVService

    @GetMapping("/allAssetsSinglePage")
    @ResponseBody
    fun exportSingleAssetsPageCSV(@RequestParam("pageN", defaultValue = "0") pageN: Int,
                     @RequestParam("order", defaultValue = "0") order: Int): ResponseEntity<Resource?>? {
        if(pageN<0) return ResponseEntity.badRequest().body(null)
        val ordering: String = when (order) {
            1 -> "DestIp4"
            2 -> "SrcMac"
            3 -> "DestMac"
            4 -> "SrcIp6"
            5 -> "DestIp6"
            else -> "SrcIp4"
        }
        val filename = "assets-page_$pageN-ordering_$ordering.csv"
        val file = InputStreamResource(csvService.singleAssetPagetoCSVFileStream(pageN, order, filename))
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .contentType(MediaType.parseMediaType("application/csv")).body(file)
    }

    @GetMapping("/all")
    @ResponseBody
    fun exportAllAssetsCSV(): ResponseEntity<Resource?>? {
        val filename = "allAssets.csv"
        val file = InputStreamResource(csvService.allAssetsToCSVFileStream(filename))
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .contentType(MediaType.parseMediaType("application/csv")).body(file)
    }

    @GetMapping("/allSRCipv4")
    @ResponseBody
    fun exportAllSRCip4CSV(): ResponseEntity<Resource?>? {
        val filename = "allSRCipv4.csv"
        val file = InputStreamResource(csvService.allSRCip4toCSVFileStream(filename))
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .contentType(MediaType.parseMediaType("application/csv")).body(file)
    }

    @GetMapping("/alldestIPv4")
    @ResponseBody
    fun exportAlldestIPv4CSV(): ResponseEntity<Resource?>? {
        val filename = "alldestIPv4.csv"
        val file = InputStreamResource(csvService.allDESTip4toCSVFileStream(filename))
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .contentType(MediaType.parseMediaType("application/csv")).body(file)
    }

    @GetMapping("/allsrcMAC")
    @ResponseBody
    fun exportAllsrcMACCSV(): ResponseEntity<Resource?>? {
        val filename = "allsrcMAC.csv"
        val file = InputStreamResource(csvService.allSrcMACtoCSVFileStream(filename))
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .contentType(MediaType.parseMediaType("application/csv")).body(file)
    }

    @GetMapping("/alldestMAC")
    @ResponseBody
    fun exportAlldestMACCSV(): ResponseEntity<Resource?>? {
        val filename = "alldestMAC.csv"
        val file = InputStreamResource(csvService.allDestMACtoCSVFileStream(filename))
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .contentType(MediaType.parseMediaType("application/csv")).body(file)
    }

    @GetMapping("/proto")
    @ResponseBody
    fun exportAllProtoCSV(): ResponseEntity<Resource?>? {
        val filename = "allProto.csv"
        val file = InputStreamResource(csvService.allPrototoCSVFileStream(filename))
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .contentType(MediaType.parseMediaType("application/csv")).body(file)
    }
}