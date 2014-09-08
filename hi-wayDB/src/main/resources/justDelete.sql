delete from hiwaydb.Userevent where id > 0;
delete from hiwaydb.Hiwayevent where id > 0;
delete from hiwaydb.File where id > 0;
delete from hiwaydb.Accesstime where id > 0;
delete from hiwaydb.Inoutput where id > 0;
delete from hiwaydb.Invocation where id > 0;
delete from hiwaydb.Task where taskid > 0;
delete from hiwaydb.Workflowrun where id > 0;


InvocCount

map:

function (doc, meta) {
  if(doc.invocId)
    {
      emit(doc.invocId,null);
   } 
}

reduce:

_count



LogEntriesForTaskOnHostSince

map:
function (doc, meta) {
  if(doc.invocId)
    {
  emit([doc.taskId, doc.hostname,doc.timestamp], null);
    }
}


getHostNames

function (doc, meta) {
  if(doc.invocId)
    {
      emit(doc.hostname,doc.hostname);
   }
 
}

getLogEntriesForTaskOnHost

getLogEntriesForTasks

getTaskIdsForWorkflow

function (doc, meta) {
  if(doc.name)
  {
     emit(doc.name, null);
  }
}

getTaskname