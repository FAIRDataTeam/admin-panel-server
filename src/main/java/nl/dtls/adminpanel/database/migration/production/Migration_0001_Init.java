/**
 * The MIT License Copyright © 2017 DTL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package nl.dtls.adminpanel.database.migration.production;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import nl.dtls.shared.Profiles;
import org.bson.Document;
import org.springframework.context.annotation.Profile;

@ChangeLog(order = "0001")
@Profile(Profiles.PRODUCTION)
public class Migration_0001_Init {

    @ChangeSet(order = "0001", id = "0001_init", author = "migrationBot")
    public void run(MongoDatabase db) {
        MongoCollection<Document> userCol = db.getCollection("user");
        userCol.insertOne(userAlbert());
    }

    private Document userAlbert() {
        Document user = new Document();
        user.append("uuid", "7e64818d-6276-46fb-8bb1-732e6e09f7e9");
        user.append("name", "Albert Einstein");
        user.append("email", "albert.einstein@example.com");
        user.append("passwordHash",
            "$2a$10$t2foZfp7cZFQo2u/33ZqTu2WNitBqYd2EY2tQO0/rBUdf8QfsAxyW"); // password
        user.append("role", "ADMIN");
        user.append("_class", "nl.dtls.fairdatapoint.entity.user.User");
        return user;
    }

}
