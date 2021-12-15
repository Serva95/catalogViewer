package it.srv.catalogViewer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CatalogViewerApplication

fun main(args: Array<String>) {
	runApplication<CatalogViewerApplication>(*args)
}
