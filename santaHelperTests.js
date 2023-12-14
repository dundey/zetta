const axios = require('axios');
const expect = require('chai').expect;

// An array with users from 1 to 10, instead of typing each UserID
const usersToTest = Array.from({ length: 10 }, (_, i) => i + 1);

describe('Santa Helper API Tests', () => {
  usersToTest.forEach(userId => {

    // First scenario to verify that each user from the array has 10 posts
    it(`User ${userId} has 10 posts`, async () => {
      const response = await axios.get('https://jsonplaceholder.typicode.com/posts');
      const userPosts = response.data.filter(post => post.userId === userId);
      expect(userPosts.length).to.equal(10); 
    });
  });

  // Second scenario to verify that each post has a unique ID
  it('Each blog post has a unique ID', async () => {
    const response = await axios.get('https://jsonplaceholder.typicode.com/posts');
    const ids = response.data.map(post => post.id);
    const uniqueIds = new Set(ids);
    expect(uniqueIds.size).to.equal(ids.length);
  });

  // Third extra scenaio to count the number of posts for each user
  it('Count the number of posts for each user', async () => {
    const response = await axios.get('https://jsonplaceholder.typicode.com/posts');
    const postCounts = response.data.reduce((counts, post) => {
      counts[post.userId] = (counts[post.userId] || 0) + 1;
      return counts;
    }, {});
    usersToTest.forEach(userId => {
      expect(postCounts[userId]).to.equal(10);
      console.log(`Counted ${postCounts[userId]} posts for user ${userId}`);
    });
  });
});
