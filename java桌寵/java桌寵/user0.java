public class user0 {
    user0(String id){
        myFirebaseRef.child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
            System.out.println(snapshot.getValue());
            }
            
            @Override public void onCancelled(FirebaseError error) { }
            
            });
    }
}
