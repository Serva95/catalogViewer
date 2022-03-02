package it.srv.catalogViewer.repo;

import it.srv.catalogViewer.model.Ipv4Info
import org.springframework.data.repository.CrudRepository

interface Ipv4InfoRepository : CrudRepository<Ipv4Info, String> {
}