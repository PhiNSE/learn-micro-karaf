package com.example.microservicea.api;

import com.example.common.model.Job;
import com.example.microservicea.service.JobCommandService;
import com.example.microservicea.service.JobQueryService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/jobs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobResource {
    private final JobCommandService commandService;
    private final JobQueryService queryService;

    public JobResource(JobCommandService commandService, JobQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @POST
    public Response create(Job job) {
        commandService.create(job);
        return Response.accepted().build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Job job) {
        commandService.update(id, job);
        return Response.accepted().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        commandService.delete(id);
        return Response.accepted().build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        Job job = queryService.getById(id);
        if (job == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(job).build();
    }
}
