package it.srv.catalogViewer

import it.srv.catalogViewer.services.CSVService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/export")
class ExportController {

    @Autowired
    lateinit var csvService: CSVService

    @GetMapping("")
    @ResponseBody
    fun exportAssetsCSV(@RequestParam("pageN", defaultValue = "0") pageN: Int,
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
}